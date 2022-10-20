package com.sdell.omega_edit.grpc

import io.grpc.Status
import java.nio.file.Paths
import java.util.concurrent.ConcurrentSkipListMap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import com.google.protobuf.ByteString

import com.ctc.omega_edit.SessionImpl
import com.ctc.omega_edit.ViewportImpl
import com.ctc.omega_edit.api.SessionCallback
import com.ctc.omega_edit.api.ViewportCallback
import com.ctc.omega_edit.api.SessionEvent as ApiSessionEvent
import com.ctc.omega_edit.api.ViewportEvent as ApiViewportEvent
import com.sdell.omega_edit.protos.*
import java.util.UUID

class Editors {
    // session and channels for sessions
    val sessions = mutableMapOf<String, Session>()
    private val sessionChannels = ConcurrentSkipListMap<String, Channel<SessionEvent>>()

    // viewports and channels for viewports
    private val viewports = mutableMapOf<String, Viewport>()
    private val viewportChannels = ConcurrentSkipListMap<String, Channel<ViewportEvent>>()

    // session id mapping to list of viewport ids
    private val sessionToViewportMap = mutableMapOf<String, MutableList<String>>()

    // get session from sessions map
    fun getSession(id: String): Session =
        sessions[id] ?: throw grpcFailure(Status.UNKNOWN, "Error finding session")

    // get viewport from viewports map
    fun getViewport(id: String): Viewport =
        viewports[id] ?: throw grpcFailure(Status.UNKNOWN, "Error finding viewport")

    // close all session channels
    fun closeSessionChannels() {
        for (id in sessionChannels.keys) {
            sessionChannels[id]?.close()
            sessionChannels.remove(id)
        }
    }

    // close all viewport channels
    fun closeViewportChannels() {
        for (id in viewportChannels.keys) {
            viewportChannels[id]?.close()
            viewportChannels.remove(id)
        }
    }

    // create session
    suspend fun createSession(sessionIdDesired: String, filePath: String): CreateSessionResponse {
        val path = if (filePath != "") Paths.get(filePath) else null
        val id = if (sessionIdDesired != "") sessionIdDesired else idFor(path)

        if (sessions.keys.contains(id))
            throw grpcFailure(Status.ALREADY_EXISTS, "Session already exists")

        // create new session channel
        createSessionChannel().also { sessionChannels[id] = it }

        // callback function for new session
        val cb = SessionCallback { session, event: ApiSessionEvent, change ->
            run {
                GlobalScope.launch {
                    // suspend needed to avoid error of channel being closed and another event trying to be sent
                    suspend {
                        sessionChannels[id]?.send(
                            SessionEvent
                                .newBuilder()
                                .setSessionId(id)
                                .setComputedFileSize(session.size())
                                .setChangeCount(session.numChanges())
                                .setUndoCount(session.numUndos())
                                .setSerial(
                                    when (change) {
                                        is scala.Some -> change.get().id()
                                        else -> 0
                                    }
                                )
                                .setSessionEventKind(SessionEventKind.forNumber(checkSessionEventType(event)))
                                .build()
                        )
                    }
                }
            }
        }

        // create new session pointer
        val sessionPointer = ffi.omega_edit_create_session(
            path?.toString(), cb, null, 0
        )

        // create new session
        sessions[id] = Session(
            SessionImpl(
                sessionPointer, ffi
            ),
            sessionPointer,
            sessionChannels[id]!!,
        )

        // initialize new session id to list of viewport ids
        sessionToViewportMap[id] = mutableListOf<String>()

        return CreateSessionResponse
            .newBuilder()
            .setSessionId(id)
            .build()
    }

    // destroy session
    fun destroySession(request: ObjectId): ObjectId {
        val id = request.id
        val session = getSession(id)

        // destroy all viewport in the session
        for (vid in sessionToViewportMap[id]!!.iterator()) {
            viewports[vid]?.destroy()
            viewports.remove(vid)
        }

        // destroy and remove session
        session.destroy()
        sessions.remove(id)

        // close and remove session channel
        sessionChannels[id]?.close()
        sessionChannels.remove(id)

        return request
    }

    // submit change to session
    fun submitChange(id: String, data: ByteString, kind: ChangeKind, offset: Long, length: Long): Long {
        val session = getSession(id)
        session.submitChange(data, kind, offset, length)
        return session.getLastChange().id()
    }

    // create viewport
    suspend fun createViewport(
        sessionId: String,
        viewportIdDesired: String,
        offset: Long,
        capacity: Long,
        isFloating: Boolean
    ): CreateViewportResponse {
        val id = if (viewportIdDesired != "") viewportIdDesired else "$sessionId;${UUID.randomUUID().toString()}"

        if (!sessions.containsKey(sessionId))
            throw grpcFailure(Status.NOT_FOUND, "Session with id: $id not found")

        if (viewports.keys.contains(id))
            throw grpcFailure(Status.ALREADY_EXISTS, "Viewport already exists")

        // create new viewport channel
        createViewportChannel().also { viewportChannels[id] = it }

        // viewport callback function for new viewport
        val cb = ViewportCallback { viewport, event: ApiViewportEvent, change ->
            run {
                GlobalScope.launch {
                    // suspend needed to avoid error of channel being closed and another event trying to be sent
                    suspend {
                        viewportChannels[id]?.send(
                            ViewportEvent
                                .newBuilder()
                                .setSessionId(sessionId)
                                .setViewportId(id)
                                .setData(ByteString.copyFrom(viewport.data()))
                                .setLength(viewport.data().size.toLong())
                                .setOffset(offset)
                                .setViewportEventKind(ViewportEventKind.forNumber(checkViewportEventType(event)))
                                .setSerial(
                                    when (change) {
                                        is scala.Some -> change.get().id()
                                        else -> 0
                                    }
                                )
                                .build()
                        )
                    }
                }
            }
        }

        // create new viewport pointer
        val viewportPointer = ffi.omega_edit_create_viewport(
            getSession(sessionId).getSessionPointer(),
            offset,
            capacity,
            isFloating,
            cb,
            null,
            0
        )

        // create new viewport
        viewports[id] = Viewport(
            ViewportImpl(viewportPointer, ffi),
            id,
            viewportPointer,
            viewportChannels[id]!!
        )

        // add viewport id to list of viewports for session id
        sessionToViewportMap[sessionId]?.add(id)

        return CreateViewportResponse
            .newBuilder()
            .setSessionId(sessionId)
            .setViewportId(id)
            .build()
    }

    // destroy viewport
    fun destroyViewport(request: ObjectId): ObjectId {
        val id = request.id
        val viewport = getViewport(id)

        // destroy and remove viewport
        viewport.destroy()
        viewports.remove(id)
        viewportChannels[id]?.close()
        viewportChannels.remove(id)

        // Remove viewport id from session to viewport map
        for (sid in sessionToViewportMap.keys) {
            if (sessionToViewportMap[sid]!!.contains(id))
                sessionToViewportMap[sid]?.remove(id)
        }

        return request
    }
}
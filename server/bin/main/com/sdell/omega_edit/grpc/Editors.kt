package com.sdell.omega_edit.grpc

// General imports
import io.grpc.Status
import java.nio.file.Paths
import java.util.concurrent.ConcurrentSkipListMap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import com.google.protobuf.ByteString

// omega-edit imports
import com.ctc.omega_edit.SessionImpl
import com.ctc.omega_edit.ViewportImpl
import com.ctc.omega_edit.api.Change
import com.ctc.omega_edit.api.SessionCallback
import com.ctc.omega_edit.api.ViewportCallback
import com.ctc.omega_edit.api.SessionEvent as ApiSessionEvent
import com.ctc.omega_edit.api.ViewportEvent as ApiViewportEvent
import com.sdell.omega_edit.protos.*
import java.util.UUID

class Editors {
    val sessions = mutableMapOf<String, Session>()
    private val sessionChannels = ConcurrentSkipListMap<String, Channel<SessionEvent>>()
    private val viewports = mutableMapOf<String, Viewport>()
    private val viewportChannels = ConcurrentSkipListMap<String, Channel<ViewportEvent>>()
    private val sessionToViewportMap = mutableMapOf<String, MutableList<String>>()

    fun getSession(id: String): Session =
        sessions[id] ?: throw grpcFailure(Status.UNKNOWN, "Error finding session")

    fun getViewport(id: String): Viewport =
        viewports[id] ?: throw grpcFailure(Status.UNKNOWN, "Error finding viewport")

    fun closeSessionChannels() {
        for (id in sessionChannels.keys) {
            sessionChannels[id]?.close()
            sessionChannels.remove(id)
        }
    }

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

        createSessionChannel().also { sessionChannels[id] = it }

        val cb = SessionCallback { session, event: ApiSessionEvent, change ->
            run {
                GlobalScope.launch {
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
                                        is scala.Some -> {
                                            try {
                                                change.get().id()
                                            } catch (e: Exception) {
                                                0
                                            }
                                        }
                                        else -> 0
                                    }
                                )
                                .setSessionEventKind(
                                    SessionEventKind
                                        .forNumber(
                                            when (event) {
                                                SessionEvents["Create"] -> 1
                                                SessionEvents["Edit"] -> 2
                                                SessionEvents["Undo"] -> 4
                                                SessionEvents["Clear"] -> 8
                                                SessionEvents["Transform"] -> 16
                                                SessionEvents["CreateCheckpoint"] -> 32
                                                SessionEvents["DestroyCheckpoint"] -> 64
                                                SessionEvents["Save"] -> 128
                                                SessionEvents["ChangesPaused"] -> 256
                                                SessionEvents["ChangesResumed"] -> 512
                                                SessionEvents["CreateViewport"] -> 1024
                                                SessionEvents["DestroyViewport"] -> 2048
                                                else -> 0
                                            }
                                        )
                                )
                                .build()
                        )
                    }
                }
            }
        }

        val sessionPointer = ffi.omega_edit_create_session(
            path?.toString(), cb, null, 0
        )

        sessions[id] = Session(
            SessionImpl(
                sessionPointer, ffi
            ),
            sessionPointer,
            sessionChannels[id]!!,
        )

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

        sessionChannels[id]?.close()
        sessionChannels.remove(id)

        return request
    }

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
        val id = if (viewportIdDesired != "") viewportIdDesired else UUID.randomUUID().toString()

        if (!sessions.containsKey(sessionId))
            throw grpcFailure(Status.NOT_FOUND, "Session with id: $id not found")

        if (viewports.keys.contains(id))
            throw grpcFailure(Status.ALREADY_EXISTS, "Viewport already exists")

        createViewportChannel().also { viewportChannels[id] = it }

        val cb = ViewportCallback { viewport, event: ApiViewportEvent, change ->
            run {
                GlobalScope.launch {
                    suspend {
                        viewportChannels[id]?.send(
                            ViewportEvent
                                .newBuilder()
                                .setSessionId(sessionId)
                                .setViewportId(id)
                                .setData(ByteString.copyFrom(viewport.data()))
                                .setLength(viewport.data().size.toLong())
                                .setOffset(offset)
                                .setSerial(
                                    when (change) {
                                        is scala.Some -> {
                                            try {
                                                change.get().id()
                                            } catch (e: Exception) {
                                                0
                                            }
                                        }
                                        else -> 0
                                    }
                                )
                                .setViewportEventKind(
                                    ViewportEventKind
                                        .forNumber(
                                            when (event) {
                                                ViewportEvents["Create"] -> 1
                                                ViewportEvents["Edit"] -> 2
                                                ViewportEvents["Undo"] -> 4
                                                ViewportEvents["Clear"] -> 8
                                                ViewportEvents["Transform"] -> 16
                                                ViewportEvents["Modify"] -> 32
                                                else -> 0
                                            }
                                        )
                                )
                                .build()
                        )
                    }
                }
            }
        }

        val viewportPointer = ffi.omega_edit_create_viewport(
            getSession(sessionId).getSessionPointer(),
            offset,
            capacity,
            isFloating,
            cb,
            null,
            0
        )

        viewports[id] = Viewport(
            ViewportImpl(viewportPointer, ffi),
            id,
            viewportPointer,
            viewportChannels[id]!!
        )

        sessionToViewportMap[sessionId]?.add(id)

        return CreateViewportResponse
            .newBuilder()
            .setSessionId(sessionId)
            .setViewportId(id)
            .build()
    }

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
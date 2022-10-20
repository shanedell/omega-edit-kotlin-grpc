package com.sdell.omega_edit.grpc

import com.ctc.omega_edit.api.Change
import com.ctc.omega_edit.api.Segment
import io.grpc.Status
import java.nio.file.Path
import java.nio.file.Paths
import arrow.core.*
import com.google.protobuf.ByteString

import com.ctc.omega_edit.api.Session as ApiSession
import com.sdell.omega_edit.protos.*
import com.sdell.omega_edit.protos.SessionEvent
import jnr.ffi.Pointer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class Session(apiSessionIn: ApiSession, sessionPointerIn: Pointer, channelIn: Channel<SessionEvent>) {
    private val apiSession = apiSessionIn
    private val sessionPointer = sessionPointerIn
    private val channel = channelIn

    // getter for private session pointer
    fun getSessionPointer(): Pointer = sessionPointer

    // destroy session
    fun destroy() = apiSession.destroy()

    // get session file size
    fun size(): Long = apiSession.size()

    // clear changes
    fun clear(): Change.Result = apiSession.clearChanges()

    // unwatch session events
    fun unwatch() = ffi.omega_session_set_event_interest(sessionPointer, 0)

    // pause session changes
    fun pauseSession() = apiSession.pauseSessionChanges()

    // resume session changes
    fun resumeSession() = apiSession.resumeSessionChanges()

    // pause session's viewport events
    fun pauseViewportEvents() = apiSession.pauseViewportEvents()

    // resume session's viewport events
    fun resumeViewportEvents() = apiSession.resumeViewportEvents()

    // undo last change, returning the last undo change id
    fun undoLast(): Long = apiSession.undoLast().run { return getLastUndo().id() }

    // redo last undo, returning the change id
    fun redoUndo(): Long = apiSession.redoUndo().run { return getLastChange().id() }

    // save session
    fun save(savePath: String, overwrite: Boolean): Path {
        val res = apiSession.save(Paths.get(savePath), overwrite)
        return if (res == null || res.isFailure) throw grpcFailure(
            Status.UNKNOWN,
            "Failed to save session"
        ) else res.get()
    }

    // get different counts based on kind of count
    fun getCount(kind: CountKind): Long =
        when (kind) {
            CountKind.COUNT_CHANGES -> apiSession.numChanges()
            CountKind.COUNT_CHECKPOINTS -> apiSession.numCheckpoints()
            CountKind.COUNT_SEARCH_CONTEXTS -> throw grpcFailure(Status.UNIMPLEMENTED)
            CountKind.COUNT_FILE_SIZE -> apiSession.size()
            CountKind.COUNT_UNDOS -> apiSession.numUndos()
            CountKind.COUNT_VIEWPORTS -> apiSession.numViewports()
            CountKind.UNDEFINED_COUNT_KIND -> throw grpcFailure(Status.UNKNOWN, "undefined kind: $kind")
            CountKind.UNRECOGNIZED -> throw grpcFailure(Status.UNKNOWN, "unrecognized kind: $kind")
            else -> throw grpcFailure(Status.UNKNOWN, "unable to compute")
        }

    // get last change in session
    fun getLastChange(): Change = when (val change = apiSession.lastChange.get().toOption()) {
        is Some -> change.value
        is None -> throw grpcFailure(Status.NOT_FOUND, "Last change couldn't be found")
    }

    // get last undo in session
    fun getLastUndo(): Change = when (val lastUndo = apiSession.lastUndo.get().toOption()) {
        is Some -> lastUndo.value
        is None -> throw grpcFailure(Status.NOT_FOUND, "Last undo couldn't be found")
    }

    // submit an insert, delete or overwrite change to the session
    fun submitChange(data: ByteString, kind: ChangeKind, offset: Long, length: Long): Change.Result =
        when (kind) {
            ChangeKind.CHANGE_DELETE -> apiSession.delete(offset, length)
            ChangeKind.CHANGE_INSERT -> apiSession.insert(data.toByteArray(), offset)
            ChangeKind.CHANGE_OVERWRITE -> apiSession.overwrite(data.toByteArray(), offset)
            else -> throw grpcFailure(Status.INVALID_ARGUMENT)
        }

    // get segment from session based on offset and length
    fun getSegment(offset: Long, length: Long): Segment =
        when (
            val segment = apiSession.getSegment(offset, length).get().toOption()
        ) {
            is Some -> segment.value
            is None -> throw grpcFailure(Status.NOT_FOUND)
        }

    // find change in session match change id
    fun findChange(id: Long): Change =
        when (val change = apiSession.findChange(id).get().toOption()) {
            is Some -> change.value
            is None -> throw grpcFailure(Status.NOT_FOUND)
        }

    // get session frequency profile
    fun getFrequencyProfile(offset: Long, length: Long): LongArray =
        when (val profile = apiSession.profile(offset, length).get().toOption()) {
            is Some -> profile.value
            is None -> LongArray(0)
        }

    // search session
    fun search(
        pattern: ByteString, offset: Long, length: Long, isCaseInsensitive: Boolean, limit: scala.Option<Any>
    ): scala.collection.immutable.List<Any> {
        return apiSession.search(
            pattern.toByteArray(),
            offset,
            length,
            isCaseInsensitive,
            limit
        )
    }

    // watch session events
    fun watch(interest: Int): Flow<SessionEvent> = ffi
        .omega_session_set_event_interest(sessionPointer, interest)
        .run {
            return channel.receiveAsFlow()
        }
}
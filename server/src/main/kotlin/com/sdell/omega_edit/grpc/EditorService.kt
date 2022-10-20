package com.sdell.omega_edit.grpc

import arrow.core.*
import io.grpc.Status
import com.google.protobuf.Empty
import com.google.protobuf.ByteString
import com.sdell.omega_edit.protos.*
import kotlinx.coroutines.flow.Flow
import com.ctc.omega_edit.api.SessionEvent.`Interest$`.`MODULE$` as Interest

class EditorService : EditorGrpcKt.EditorCoroutineImplBase() {
    private val editors = Editors()

    // close session and viewport channels
    fun closeChannels() {
        editors.closeSessionChannels()
        editors.closeViewportChannels()
    }

    override suspend fun getVersion(request: Empty): VersionResponse =
        OmegaEdit.version().run {
            return VersionResponse
                .newBuilder()
                .setMajor(major())
                .setMinor(minor())
                .setPatch(patch())
                .build()
        }

    override suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse =
        editors.createSession(request.sessionIdDesired, request.filePath)

    override suspend fun destroySession(request: ObjectId): ObjectId = editors.destroySession(request)

    override suspend fun saveSession(request: SaveSessionRequest): SaveSessionResponse =
        SaveSessionResponse
            .newBuilder()
            .setSessionId(request.sessionId)
            .setFilePath(
                editors
                    .getSession(request.sessionId)
                    .save(request.filePath, request.allowOverwrite)
                    .toString()
            )
            .build()

    override suspend fun createViewport(request: CreateViewportRequest): CreateViewportResponse =
        editors.createViewport(
            request.sessionId,
            request.viewportIdDesired,
            request.offset,
            request.capacity,
            request.isFloating
        )

    override suspend fun destroyViewport(request: ObjectId): ObjectId = editors.destroyViewport(request)

    override suspend fun getViewportData(request: ViewportDataRequest): ViewportDataResponse =
        editors.getViewport(request.viewportId).getViewportData()

    override suspend fun submitChange(request: ChangeRequest): ChangeResponse {
        editors.submitChange(
            request.sessionId, request.data, request.kind, request.offset, request.length
        ).run {
            return ChangeResponse
                .newBuilder()
                .setSessionId(request.sessionId)
                .setSerial(this)
                .build()
        }
    }

    override suspend fun getChangeDetails(request: SessionEvent): ChangeDetailsResponse =
        when (request.serial.toOption()) {
            is Some -> editors.getSession(request.sessionId).findChange(request.serial)
            is None -> throw grpcFailure(Status.INVALID_ARGUMENT, "change serial id required")
        }.run {
            return ChangeDetailsResponse
                .newBuilder()
                .setSessionId(request.sessionId)
                .setSerial(id())
                .setOffset(offset())
                .setLength(length())
                .setData(ByteString.copyFrom(data()))
                .setKind(
                    when (operation()) {
                        ChangeKt.Delete -> ChangeKind.CHANGE_DELETE
                        ChangeKt.Insert -> ChangeKind.CHANGE_INSERT
                        ChangeKt.Overwrite -> ChangeKind.CHANGE_OVERWRITE
                        else -> ChangeKind.UNDEFINED_CHANGE
                    }
                )
                .build()
        }

    override suspend fun getComputedFileSize(request: ObjectId): ComputedFileSizeResponse =
        ComputedFileSizeResponse
            .newBuilder()
            .setSessionId(request.id)
            .setComputedFileSize(
                ffi.omega_session_get_computed_file_size(
                    editors.getSession(request.id).getSessionPointer()
                )
            )
            .build()

    override suspend fun getSessionCount(request: Empty): SessionCountResponse =
        SessionCountResponse
            .newBuilder()
            .setCount(editors.sessions.size.toLong())
            .build()

    override suspend fun getCount(request: CountRequest): CountResponse =
        CountResponse
            .newBuilder()
            .setSessionId(request.sessionId)
            .setKind(request.kind)
            .setCount(
                editors.getSession(request.sessionId).getCount(request.kind)
            )
            .build()

    override fun subscribeToSessionEvents(request: EventSubscriptionRequest): Flow<SessionEvent> =
        editors.getSession(request.id).watch(
            if (request.hasInterest()) request.interest else Interest.All()
        )

    override fun subscribeToViewportEvents(request: EventSubscriptionRequest): Flow<ViewportEvent> =
        editors.getViewport(request.id).watch(
            if (request.hasInterest()) request.interest else Interest.All()
        )

    override suspend fun unsubscribeToSessionEvents(request: ObjectId): ObjectId =
        editors.getSession(request.id).unwatch().run { return request }

    override suspend fun unsubscribeToViewportEvents(request: ObjectId): ObjectId =
        editors.getViewport(request.id).unwatch().run { return request }

    override suspend fun getByteFrequencyProfile(request: ByteFrequencyProfileRequest): ByteFrequencyProfileResponse {
        val response = ByteFrequencyProfileResponse
            .newBuilder()
            .setSessionId(request.sessionId)
            .setOffset(request.offset)
            .setLength(request.length)
        val profile = editors
            .getSession(request.sessionId)
            .getFrequencyProfile(request.offset, request.length)

        for (i in profile) {
            response.addFrequency(i)
        }

        return response.build()
    }

    override suspend fun searchSession(request: SearchRequest): SearchResponse {
        val response = SearchResponse
            .newBuilder()
            .setSessionId(request.sessionId)
            .setPattern(request.pattern)
            .setIsCaseInsensitive(request.isCaseInsensitive)
            .setOffset(request.offset)
            .setLength(request.length)

        val searchResults = editors
            .getSession(request.sessionId)
            .search(
                request.pattern,
                request.offset,
                request.length,
                request.isCaseInsensitive,
                scala.Option.apply(if (request.limit > 0) request.limit else null)
            )

        for (i in searchResults) {
            response.addMatchOffset(i.toString().toLong())
        }

        return response.build()
    }

    override suspend fun undoLastChange(request: ObjectId): ChangeResponse =
        editors.getSession(request.id).undoLast().run {
            return ChangeResponse
                .newBuilder()
                .setSessionId(request.id)
                .setSerial(this)
                .build()
        }

    override suspend fun redoLastUndo(request: ObjectId): ChangeResponse =
        editors.getSession(request.id).redoUndo().run {
            return ChangeResponse
                .newBuilder()
                .setSessionId(request.id)
                .setSerial(this)
                .build()
        }

    override suspend fun clearChanges(request: ObjectId): ObjectId =
        editors.getSession(request.id).clear().run {
            return request
        }

    override suspend fun getLastChange(request: ObjectId): ChangeDetailsResponse =
        editors.getSession(request.id).getLastChange().run {
            return ChangeDetailsResponse
                .newBuilder()
                .setSessionId(request.id)
                .setSerial(id())
                .setOffset(offset())
                .setLength(length())
                .setData(ByteString.copyFrom(data()))
                .setKind(
                    when (operation()) {
                        ChangeKt.Delete -> ChangeKind.CHANGE_DELETE
                        ChangeKt.Insert -> ChangeKind.CHANGE_INSERT
                        ChangeKt.Overwrite -> ChangeKind.CHANGE_OVERWRITE
                        else -> ChangeKind.UNDEFINED_CHANGE
                    }
                )
                .build()
        }

    override suspend fun getLastUndo(request: ObjectId): ChangeDetailsResponse =
        editors.getSession(request.id).getLastUndo().run {
            return ChangeDetailsResponse
                .newBuilder()
                .setSessionId(request.id)
                .setSerial(id())
                .setOffset(offset())
                .setLength(length())
                .setData(ByteString.copyFrom(data()))
                .setKind(
                    when (operation()) {
                        ChangeKt.Delete -> ChangeKind.CHANGE_DELETE
                        ChangeKt.Insert -> ChangeKind.CHANGE_INSERT
                        ChangeKt.Overwrite -> ChangeKind.CHANGE_OVERWRITE
                        else -> ChangeKind.UNDEFINED_CHANGE
                    }
                )
                .build()
        }

    override suspend fun pauseSessionChanges(request: ObjectId): ObjectId =
        editors.getSession(request.id).pauseSession().run { return request }

    override suspend fun resumeSessionChanges(request: ObjectId): ObjectId =
        editors.getSession(request.id).resumeSession().run { return request }

    override suspend fun pauseViewportEvents(request: ObjectId): ObjectId =
        editors.getSession(request.id).pauseViewportEvents().run { return request }

    override suspend fun resumeViewportEvents(request: ObjectId): ObjectId =
        editors.getSession(request.id).resumeViewportEvents().run { return request }

    override suspend fun getSegment(request: SegmentRequest): SegmentResponse =
        editors
            .getSession(request.sessionId)
            .getSegment(request.offset, request.length)
            .run {
                return SegmentResponse
                    .newBuilder()
                    .setSessionId(request.sessionId)
                    .setOffset(offset())
                    .setData(ByteString.copyFrom(data()))
                    .build()
            }
}
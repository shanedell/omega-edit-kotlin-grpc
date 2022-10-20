package com.sdell.omega_edit.protos

import com.google.protobuf.Empty
import com.sdell.omega_edit.protos.EditorGrpc.getServiceDescriptor
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ClientCalls.serverStreamingRpc
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls
import io.grpc.kotlin.ServerCalls.serverStreamingServerMethodDefinition
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.String
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlinx.coroutines.flow.Flow

/**
 * Holder for Kotlin coroutine-based client and server APIs for omega_edit.Editor.
 */
public object EditorGrpcKt {
  public const val SERVICE_NAME: String = EditorGrpc.SERVICE_NAME

  @JvmStatic
  public val serviceDescriptor: ServiceDescriptor
    get() = EditorGrpc.getServiceDescriptor()

  public val getVersionMethod: MethodDescriptor<Empty, VersionResponse>
    @JvmStatic
    get() = EditorGrpc.getGetVersionMethod()

  public val createSessionMethod: MethodDescriptor<CreateSessionRequest, CreateSessionResponse>
    @JvmStatic
    get() = EditorGrpc.getCreateSessionMethod()

  public val saveSessionMethod: MethodDescriptor<SaveSessionRequest, SaveSessionResponse>
    @JvmStatic
    get() = EditorGrpc.getSaveSessionMethod()

  public val destroySessionMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getDestroySessionMethod()

  public val submitChangeMethod: MethodDescriptor<ChangeRequest, ChangeResponse>
    @JvmStatic
    get() = EditorGrpc.getSubmitChangeMethod()

  public val undoLastChangeMethod: MethodDescriptor<ObjectId, ChangeResponse>
    @JvmStatic
    get() = EditorGrpc.getUndoLastChangeMethod()

  public val redoLastUndoMethod: MethodDescriptor<ObjectId, ChangeResponse>
    @JvmStatic
    get() = EditorGrpc.getRedoLastUndoMethod()

  public val clearChangesMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getClearChangesMethod()

  public val pauseSessionChangesMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getPauseSessionChangesMethod()

  public val resumeSessionChangesMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getResumeSessionChangesMethod()

  public val pauseViewportEventsMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getPauseViewportEventsMethod()

  public val resumeViewportEventsMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getResumeViewportEventsMethod()

  public val createViewportMethod: MethodDescriptor<CreateViewportRequest, CreateViewportResponse>
    @JvmStatic
    get() = EditorGrpc.getCreateViewportMethod()

  public val getViewportDataMethod: MethodDescriptor<ViewportDataRequest, ViewportDataResponse>
    @JvmStatic
    get() = EditorGrpc.getGetViewportDataMethod()

  public val destroyViewportMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getDestroyViewportMethod()

  public val getChangeDetailsMethod: MethodDescriptor<SessionEvent, ChangeDetailsResponse>
    @JvmStatic
    get() = EditorGrpc.getGetChangeDetailsMethod()

  public val getLastChangeMethod: MethodDescriptor<ObjectId, ChangeDetailsResponse>
    @JvmStatic
    get() = EditorGrpc.getGetLastChangeMethod()

  public val getLastUndoMethod: MethodDescriptor<ObjectId, ChangeDetailsResponse>
    @JvmStatic
    get() = EditorGrpc.getGetLastUndoMethod()

  public val getComputedFileSizeMethod: MethodDescriptor<ObjectId, ComputedFileSizeResponse>
    @JvmStatic
    get() = EditorGrpc.getGetComputedFileSizeMethod()

  public val getCountMethod: MethodDescriptor<CountRequest, CountResponse>
    @JvmStatic
    get() = EditorGrpc.getGetCountMethod()

  public val getSessionCountMethod: MethodDescriptor<Empty, SessionCountResponse>
    @JvmStatic
    get() = EditorGrpc.getGetSessionCountMethod()

  public val getSegmentMethod: MethodDescriptor<SegmentRequest, SegmentResponse>
    @JvmStatic
    get() = EditorGrpc.getGetSegmentMethod()

  public val searchSessionMethod: MethodDescriptor<SearchRequest, SearchResponse>
    @JvmStatic
    get() = EditorGrpc.getSearchSessionMethod()

  public val getByteFrequencyProfileMethod:
      MethodDescriptor<ByteFrequencyProfileRequest, ByteFrequencyProfileResponse>
    @JvmStatic
    get() = EditorGrpc.getGetByteFrequencyProfileMethod()

  public val subscribeToSessionEventsMethod:
      MethodDescriptor<EventSubscriptionRequest, SessionEvent>
    @JvmStatic
    get() = EditorGrpc.getSubscribeToSessionEventsMethod()

  public val subscribeToViewportEventsMethod:
      MethodDescriptor<EventSubscriptionRequest, ViewportEvent>
    @JvmStatic
    get() = EditorGrpc.getSubscribeToViewportEventsMethod()

  public val unsubscribeToSessionEventsMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getUnsubscribeToSessionEventsMethod()

  public val unsubscribeToViewportEventsMethod: MethodDescriptor<ObjectId, ObjectId>
    @JvmStatic
    get() = EditorGrpc.getUnsubscribeToViewportEventsMethod()

  /**
   * A stub for issuing RPCs to a(n) omega_edit.Editor service as suspending coroutines.
   */
  @StubFor(EditorGrpc::class)
  public class EditorCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT,
  ) : AbstractCoroutineStub<EditorCoroutineStub>(channel, callOptions) {
    public override fun build(channel: Channel, callOptions: CallOptions): EditorCoroutineStub =
        EditorCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getVersion(request: Empty, headers: Metadata = Metadata()): VersionResponse =
        unaryRpc(
      channel,
      EditorGrpc.getGetVersionMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun createSession(request: CreateSessionRequest, headers: Metadata = Metadata()):
        CreateSessionResponse = unaryRpc(
      channel,
      EditorGrpc.getCreateSessionMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun saveSession(request: SaveSessionRequest, headers: Metadata = Metadata()):
        SaveSessionResponse = unaryRpc(
      channel,
      EditorGrpc.getSaveSessionMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun destroySession(request: ObjectId, headers: Metadata = Metadata()): ObjectId =
        unaryRpc(
      channel,
      EditorGrpc.getDestroySessionMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun submitChange(request: ChangeRequest, headers: Metadata = Metadata()):
        ChangeResponse = unaryRpc(
      channel,
      EditorGrpc.getSubmitChangeMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun undoLastChange(request: ObjectId, headers: Metadata = Metadata()):
        ChangeResponse = unaryRpc(
      channel,
      EditorGrpc.getUndoLastChangeMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun redoLastUndo(request: ObjectId, headers: Metadata = Metadata()):
        ChangeResponse = unaryRpc(
      channel,
      EditorGrpc.getRedoLastUndoMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun clearChanges(request: ObjectId, headers: Metadata = Metadata()): ObjectId =
        unaryRpc(
      channel,
      EditorGrpc.getClearChangesMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun pauseSessionChanges(request: ObjectId, headers: Metadata = Metadata()):
        ObjectId = unaryRpc(
      channel,
      EditorGrpc.getPauseSessionChangesMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun resumeSessionChanges(request: ObjectId, headers: Metadata = Metadata()):
        ObjectId = unaryRpc(
      channel,
      EditorGrpc.getResumeSessionChangesMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun pauseViewportEvents(request: ObjectId, headers: Metadata = Metadata()):
        ObjectId = unaryRpc(
      channel,
      EditorGrpc.getPauseViewportEventsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun resumeViewportEvents(request: ObjectId, headers: Metadata = Metadata()):
        ObjectId = unaryRpc(
      channel,
      EditorGrpc.getResumeViewportEventsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun createViewport(request: CreateViewportRequest, headers: Metadata =
        Metadata()): CreateViewportResponse = unaryRpc(
      channel,
      EditorGrpc.getCreateViewportMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getViewportData(request: ViewportDataRequest, headers: Metadata =
        Metadata()): ViewportDataResponse = unaryRpc(
      channel,
      EditorGrpc.getGetViewportDataMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun destroyViewport(request: ObjectId, headers: Metadata = Metadata()): ObjectId
        = unaryRpc(
      channel,
      EditorGrpc.getDestroyViewportMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getChangeDetails(request: SessionEvent, headers: Metadata = Metadata()):
        ChangeDetailsResponse = unaryRpc(
      channel,
      EditorGrpc.getGetChangeDetailsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getLastChange(request: ObjectId, headers: Metadata = Metadata()):
        ChangeDetailsResponse = unaryRpc(
      channel,
      EditorGrpc.getGetLastChangeMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getLastUndo(request: ObjectId, headers: Metadata = Metadata()):
        ChangeDetailsResponse = unaryRpc(
      channel,
      EditorGrpc.getGetLastUndoMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getComputedFileSize(request: ObjectId, headers: Metadata = Metadata()):
        ComputedFileSizeResponse = unaryRpc(
      channel,
      EditorGrpc.getGetComputedFileSizeMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getCount(request: CountRequest, headers: Metadata = Metadata()):
        CountResponse = unaryRpc(
      channel,
      EditorGrpc.getGetCountMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getSessionCount(request: Empty, headers: Metadata = Metadata()):
        SessionCountResponse = unaryRpc(
      channel,
      EditorGrpc.getGetSessionCountMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getSegment(request: SegmentRequest, headers: Metadata = Metadata()):
        SegmentResponse = unaryRpc(
      channel,
      EditorGrpc.getGetSegmentMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun searchSession(request: SearchRequest, headers: Metadata = Metadata()):
        SearchResponse = unaryRpc(
      channel,
      EditorGrpc.getSearchSessionMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun getByteFrequencyProfile(request: ByteFrequencyProfileRequest,
        headers: Metadata = Metadata()): ByteFrequencyProfileResponse = unaryRpc(
      channel,
      EditorGrpc.getGetByteFrequencyProfileMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Returns a [Flow] that, when collected, executes this RPC and emits responses from the
     * server as they arrive.  That flow finishes normally if the server closes its response with
     * [`Status.OK`][Status], and fails by throwing a [StatusException] otherwise.  If
     * collecting the flow downstream fails exceptionally (including via cancellation), the RPC
     * is cancelled with that exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return A flow that, when collected, emits the responses from the server.
     */
    public fun subscribeToSessionEvents(request: EventSubscriptionRequest, headers: Metadata =
        Metadata()): Flow<SessionEvent> = serverStreamingRpc(
      channel,
      EditorGrpc.getSubscribeToSessionEventsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Returns a [Flow] that, when collected, executes this RPC and emits responses from the
     * server as they arrive.  That flow finishes normally if the server closes its response with
     * [`Status.OK`][Status], and fails by throwing a [StatusException] otherwise.  If
     * collecting the flow downstream fails exceptionally (including via cancellation), the RPC
     * is cancelled with that exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return A flow that, when collected, emits the responses from the server.
     */
    public fun subscribeToViewportEvents(request: EventSubscriptionRequest, headers: Metadata =
        Metadata()): Flow<ViewportEvent> = serverStreamingRpc(
      channel,
      EditorGrpc.getSubscribeToViewportEventsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun unsubscribeToSessionEvents(request: ObjectId, headers: Metadata =
        Metadata()): ObjectId = unaryRpc(
      channel,
      EditorGrpc.getUnsubscribeToSessionEventsMethod(),
      request,
      callOptions,
      headers
    )

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @param headers Metadata to attach to the request.  Most users will not need this.
     *
     * @return The single response from the server.
     */
    public suspend fun unsubscribeToViewportEvents(request: ObjectId, headers: Metadata =
        Metadata()): ObjectId = unaryRpc(
      channel,
      EditorGrpc.getUnsubscribeToViewportEventsMethod(),
      request,
      callOptions,
      headers
    )
  }

  /**
   * Skeletal implementation of the omega_edit.Editor service based on Kotlin coroutines.
   */
  public abstract class EditorCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for omega_edit.Editor.GetVersion.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getVersion(request: Empty): VersionResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetVersion is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.CreateSession.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse =
        throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.CreateSession is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.SaveSession.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun saveSession(request: SaveSessionRequest): SaveSessionResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.SaveSession is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.DestroySession.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun destroySession(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.DestroySession is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.SubmitChange.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun submitChange(request: ChangeRequest): ChangeResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.SubmitChange is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.UndoLastChange.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun undoLastChange(request: ObjectId): ChangeResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.UndoLastChange is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.RedoLastUndo.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun redoLastUndo(request: ObjectId): ChangeResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.RedoLastUndo is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.ClearChanges.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun clearChanges(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.ClearChanges is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.PauseSessionChanges.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun pauseSessionChanges(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.PauseSessionChanges is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.ResumeSessionChanges.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun resumeSessionChanges(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.ResumeSessionChanges is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.PauseViewportEvents.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun pauseViewportEvents(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.PauseViewportEvents is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.ResumeViewportEvents.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun resumeViewportEvents(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.ResumeViewportEvents is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.CreateViewport.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun createViewport(request: CreateViewportRequest): CreateViewportResponse =
        throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.CreateViewport is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetViewportData.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getViewportData(request: ViewportDataRequest): ViewportDataResponse =
        throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetViewportData is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.DestroyViewport.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun destroyViewport(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.DestroyViewport is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetChangeDetails.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getChangeDetails(request: SessionEvent): ChangeDetailsResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetChangeDetails is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetLastChange.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getLastChange(request: ObjectId): ChangeDetailsResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetLastChange is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetLastUndo.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getLastUndo(request: ObjectId): ChangeDetailsResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetLastUndo is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetComputedFileSize.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getComputedFileSize(request: ObjectId): ComputedFileSizeResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetComputedFileSize is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetCount.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getCount(request: CountRequest): CountResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetCount is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetSessionCount.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getSessionCount(request: Empty): SessionCountResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetSessionCount is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetSegment.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getSegment(request: SegmentRequest): SegmentResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetSegment is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.SearchSession.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun searchSession(request: SearchRequest): SearchResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.SearchSession is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.GetByteFrequencyProfile.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun getByteFrequencyProfile(request: ByteFrequencyProfileRequest):
        ByteFrequencyProfileResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.GetByteFrequencyProfile is unimplemented"))

    /**
     * Returns a [Flow] of responses to an RPC for omega_edit.Editor.SubscribeToSessionEvents.
     *
     * If creating or collecting the returned flow fails with a [StatusException], the RPC
     * will fail with the corresponding [Status].  If it fails with a
     * [java.util.concurrent.CancellationException], the RPC will fail with status
     * `Status.CANCELLED`.  If creating
     * or collecting the returned flow fails for any other reason, the RPC will fail with
     * `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open fun subscribeToSessionEvents(request: EventSubscriptionRequest): Flow<SessionEvent>
        = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.SubscribeToSessionEvents is unimplemented"))

    /**
     * Returns a [Flow] of responses to an RPC for omega_edit.Editor.SubscribeToViewportEvents.
     *
     * If creating or collecting the returned flow fails with a [StatusException], the RPC
     * will fail with the corresponding [Status].  If it fails with a
     * [java.util.concurrent.CancellationException], the RPC will fail with status
     * `Status.CANCELLED`.  If creating
     * or collecting the returned flow fails for any other reason, the RPC will fail with
     * `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open fun subscribeToViewportEvents(request: EventSubscriptionRequest):
        Flow<ViewportEvent> = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.SubscribeToViewportEvents is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.UnsubscribeToSessionEvents.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun unsubscribeToSessionEvents(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.UnsubscribeToSessionEvents is unimplemented"))

    /**
     * Returns the response to an RPC for omega_edit.Editor.UnsubscribeToViewportEvents.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    public open suspend fun unsubscribeToViewportEvents(request: ObjectId): ObjectId = throw
        StatusException(UNIMPLEMENTED.withDescription("Method omega_edit.Editor.UnsubscribeToViewportEvents is unimplemented"))

    public final override fun bindService(): ServerServiceDefinition =
        builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetVersionMethod(),
      implementation = ::getVersion
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getCreateSessionMethod(),
      implementation = ::createSession
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getSaveSessionMethod(),
      implementation = ::saveSession
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getDestroySessionMethod(),
      implementation = ::destroySession
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getSubmitChangeMethod(),
      implementation = ::submitChange
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getUndoLastChangeMethod(),
      implementation = ::undoLastChange
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getRedoLastUndoMethod(),
      implementation = ::redoLastUndo
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getClearChangesMethod(),
      implementation = ::clearChanges
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getPauseSessionChangesMethod(),
      implementation = ::pauseSessionChanges
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getResumeSessionChangesMethod(),
      implementation = ::resumeSessionChanges
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getPauseViewportEventsMethod(),
      implementation = ::pauseViewportEvents
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getResumeViewportEventsMethod(),
      implementation = ::resumeViewportEvents
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getCreateViewportMethod(),
      implementation = ::createViewport
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetViewportDataMethod(),
      implementation = ::getViewportData
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getDestroyViewportMethod(),
      implementation = ::destroyViewport
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetChangeDetailsMethod(),
      implementation = ::getChangeDetails
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetLastChangeMethod(),
      implementation = ::getLastChange
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetLastUndoMethod(),
      implementation = ::getLastUndo
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetComputedFileSizeMethod(),
      implementation = ::getComputedFileSize
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetCountMethod(),
      implementation = ::getCount
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetSessionCountMethod(),
      implementation = ::getSessionCount
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetSegmentMethod(),
      implementation = ::getSegment
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getSearchSessionMethod(),
      implementation = ::searchSession
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getGetByteFrequencyProfileMethod(),
      implementation = ::getByteFrequencyProfile
    ))
      .addMethod(serverStreamingServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getSubscribeToSessionEventsMethod(),
      implementation = ::subscribeToSessionEvents
    ))
      .addMethod(serverStreamingServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getSubscribeToViewportEventsMethod(),
      implementation = ::subscribeToViewportEvents
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getUnsubscribeToSessionEventsMethod(),
      implementation = ::unsubscribeToSessionEvents
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EditorGrpc.getUnsubscribeToViewportEventsMethod(),
      implementation = ::unsubscribeToViewportEvents
    )).build()
  }
}

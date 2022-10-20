package com.sdell.omega_edit.grpc

import com.sdell.omega_edit.protos.ViewportEvent
import com.google.protobuf.ByteString
import com.sdell.omega_edit.protos.ViewportDataResponse
import jnr.ffi.Pointer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import com.ctc.omega_edit.api.Viewport as ApiViewport

class Viewport(
    apiViewportIn: ApiViewport,
    viewportIdIn: String,
    viewportPointerIn: Pointer,
    channelIn: Channel<ViewportEvent>
) {
    private val apiViewport = apiViewportIn
    private val viewportId = viewportIdIn
    private val viewportPointer = viewportPointerIn
    private val channel = channelIn

    fun destroy() = apiViewport.destroy()
    fun unwatch() = ffi.omega_viewport_set_event_interest(viewportPointer, 0)

    fun getViewportData(): ViewportDataResponse =
        ViewportDataResponse
            .newBuilder()
            .setViewportId(viewportId)
            .setOffset(apiViewport.offset())
            .setLength(apiViewport.data().size.toLong())
            .setData(ByteString.copyFrom(apiViewport.data()))
            .build()

    fun watch(interest: Int): Flow<ViewportEvent> = ffi
        .omega_viewport_set_event_interest(viewportPointer, interest)
        .run {
            return channel.receiveAsFlow()
        }
}
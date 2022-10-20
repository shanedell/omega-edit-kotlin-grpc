package com.sdell.omega_edit.grpc

import com.sdell.omega_edit.protos.SessionEvent
import com.sdell.omega_edit.protos.ViewportEvent
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.channels.Channel
import java.nio.file.Path
import java.util.*

fun idFor(path: Path?): String = when (path) {
    null -> UUID.randomUUID().toString()
    else -> Base64.getEncoder().encodeToString(path.toString().toByteArray())
}

fun grpcFailure(status: Status, message: String = ""): StatusException =
    StatusException(
        if (message != "") status.withDescription(message) else status
    )

fun createSessionChannel() = Channel<SessionEvent>()
//.apply {
//    invokeOnClose {
//        println("Channel closed")
//    }
//}

fun createViewportChannel() = Channel<ViewportEvent>()
//.apply {
//    invokeOnClose {
//        println("Channel closed")
//    }
//}
package com.sdell.omega_edit.grpc

import com.sdell.omega_edit.protos.SessionEvent
import com.sdell.omega_edit.protos.ViewportEvent
import com.ctc.omega_edit.api.SessionEvent as ApiSessionEvent
import com.ctc.omega_edit.api.ViewportEvent as ApiViewportEvent
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.channels.Channel
import java.nio.file.Path
import java.util.*

// create id based on path
fun idFor(path: Path?): String = when (path) {
    null -> UUID.randomUUID().toString()
    else -> Base64.getEncoder().encodeToString(path.toString().toByteArray())
}

// generate a grpc exception
fun grpcFailure(status: Status, message: String = ""): StatusException =
    StatusException(
        if (message != "") status.withDescription(message) else status
    )

// create session channel, commented out code below this shows how to add additional items on close
fun createSessionChannel() = Channel<SessionEvent>()
//.apply {
//    invokeOnClose {
//        println("Channel closed")
//    }
//}

// create viewport channel, commented out code below this shows how to add additional items on close
fun createViewportChannel() = Channel<ViewportEvent>()
//.apply {
//    invokeOnClose {
//        println("Channel closed")
//    }
//}


// check session event type, returning proper integer value associated with that type
fun checkSessionEventType(event: ApiSessionEvent): Int = when (event) {
    SessionEventKt.Create -> 1
    SessionEventKt.Edit -> 2
    SessionEventKt.Undo -> 4
    SessionEventKt.Clear -> 8
    SessionEventKt.Transform -> 16
    SessionEventKt.CreateCheckpoint -> 32
    SessionEventKt.DestroyCheckpoint -> 64
    SessionEventKt.Save -> 128
    SessionEventKt.ChangesPaused -> 256
    SessionEventKt.ChangesResumed -> 512
    SessionEventKt.CreateViewport -> 1024
    SessionEventKt.DestroyViewport -> 2048
    else -> 0
}

// check viewport event type, returning proper integer value associated with that type
fun checkViewportEventType(event: ApiViewportEvent): Int = when (event) {
    ViewportEventKt.Create -> 1
    ViewportEventKt.Edit -> 2
    ViewportEventKt.Undo -> 4
    ViewportEventKt.Clear -> 8
    ViewportEventKt.Transform -> 16
    ViewportEventKt.Modify -> 32
    else -> 0
}

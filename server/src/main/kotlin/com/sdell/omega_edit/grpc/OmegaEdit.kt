package com.sdell.omega_edit.grpc

import com.ctc.omega_edit.api.Change.`Delete$`
import com.ctc.omega_edit.api.Change.`Insert$`
import com.ctc.omega_edit.api.Change.`Overwrite$`
import com.ctc.omega_edit.api.`OmegaEdit$`
import com.ctc.omega_edit.`FFI$`.`MODULE$` as FFI
import com.ctc.omega_edit.api.SessionEvent as ApiSessionEvent
import com.ctc.omega_edit.api.ViewportEvent as ApiViewportEvent

/*
 * These variables allow us to use some items from the omega-edit
 * api that otherwise wouldn't be available.
 */

val OmegaEdit = `OmegaEdit$`.`MODULE$`

// create ffi variable
val ffi = FFI.i()

/*
 * kotlin object wrapper for APIs. This ensures these items can be used
 * properly session event wrapper map for api values
 */

// change wrapper for api values
object ChangeKt {
    val Delete = `Delete$`.`MODULE$`!!
    val Insert = `Insert$`.`MODULE$`!!
    val Overwrite = `Overwrite$`.`MODULE$`!!
}

// session event wrapper map for api values
object SessionEventKt {
    val Undefined = ApiSessionEvent.`Undefined$`.`MODULE$`!!
    val Create = ApiSessionEvent.`Create$`.`MODULE$`!!
    val Edit = ApiSessionEvent.`Edit$`.`MODULE$`!!
    val Undo = ApiSessionEvent.`Undo$`.`MODULE$`!!
    val Clear = ApiSessionEvent.`Clear$`.`MODULE$`!!
    val Transform = ApiSessionEvent.`Transform$`.`MODULE$`!!
    val CreateCheckpoint = ApiSessionEvent.`CreateCheckpoint$`.`MODULE$`!!
    val DestroyCheckpoint = ApiSessionEvent.`DestroyCheckpoint$`.`MODULE$`!!
    val Save = ApiSessionEvent.`Save$`.`MODULE$`!!
    val ChangesPaused = ApiSessionEvent.`ChangesPaused$`.`MODULE$`!!
    val ChangesResumed = ApiSessionEvent.`ChangesResumed$`.`MODULE$`!!
    val CreateViewport = ApiSessionEvent.`CreateViewport$`.`MODULE$`!!
    val DestroyViewport = ApiSessionEvent.`DestroyViewport$`.`MODULE$`!!
}

// viewport event wrapper map for api values
object ViewportEventKt {
    val Undefined = ApiViewportEvent.`Undefined$`.`MODULE$`!!
    val Create = ApiViewportEvent.`Create$`.`MODULE$`!!
    val Edit = ApiViewportEvent.`Edit$`.`MODULE$`!!
    val Undo = ApiViewportEvent.`Undo$`.`MODULE$`!!
    val Clear = ApiViewportEvent.`Clear$`.`MODULE$`!!
    val Transform = ApiViewportEvent.`Transform$`.`MODULE$`!!
    val Modify = ApiViewportEvent.`Modify$`.`MODULE$`!!
}

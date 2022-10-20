package com.sdell.omega_edit.grpc

import com.ctc.omega_edit.api.Change.`Delete$`
import com.ctc.omega_edit.api.Change.`Insert$`
import com.ctc.omega_edit.api.Change.`Overwrite$`
import com.ctc.omega_edit.api.`OmegaEdit$`
import com.ctc.omega_edit.`FFI$`.`MODULE$` as FFI
import com.ctc.omega_edit.api.SessionEvent as ApiSessionEvent
import com.ctc.omega_edit.api.ViewportEvent as ApiViewportEvent

// These variables allow us to use some items from the omega-edit api that
// otherwise wouldn't be available
val OmegaEdit = `OmegaEdit$`.`MODULE$`

val ChangeDelete = `Delete$`.`MODULE$`
val ChangeInsert = `Insert$`.`MODULE$`
val ChangeOverwrite = `Overwrite$`.`MODULE$`

val ffi = FFI.i()

val SessionEvents = mapOf(
    "Undefined" to ApiSessionEvent.`Undefined$`.`MODULE$`,
    "Create" to ApiSessionEvent.`Create$`.`MODULE$`,
    "Edit" to ApiSessionEvent.`Edit$`.`MODULE$`,
    "Undo" to ApiSessionEvent.`Undo$`.`MODULE$`,
    "Clear" to ApiSessionEvent.`Clear$`.`MODULE$`,
    "Transform" to ApiSessionEvent.`Transform$`.`MODULE$`,
    "CreateCheckpoint" to ApiSessionEvent.`CreateCheckpoint$`.`MODULE$`,
    "DestroyCheckpoint" to ApiSessionEvent.`DestroyCheckpoint$`.`MODULE$`,
    "Save" to ApiSessionEvent.`Save$`.`MODULE$`,
    "ChangesPaused" to ApiSessionEvent.`ChangesPaused$`.`MODULE$`,
    "ChangesResumed" to ApiSessionEvent.`ChangesResumed$`.`MODULE$`,
    "CreateViewport" to ApiSessionEvent.`CreateViewport$`.`MODULE$`,
    "DestroyViewport" to ApiSessionEvent.`DestroyViewport$`.`MODULE$`,
)

val ViewportEvents = mapOf(
    "Undefined" to ApiViewportEvent.`Undefined$`.`MODULE$`,
    "Create" to ApiViewportEvent.`Create$`.`MODULE$`,
    "Edit" to ApiViewportEvent.`Edit$`.`MODULE$`,
    "Undo" to ApiViewportEvent.`Undo$`.`MODULE$`,
    "Clear" to ApiViewportEvent.`Clear$`.`MODULE$`,
    "Transform" to ApiViewportEvent.`Transform$`.`MODULE$`,
    "Modify" to ApiViewportEvent.`Modify$`.`MODULE$`
)

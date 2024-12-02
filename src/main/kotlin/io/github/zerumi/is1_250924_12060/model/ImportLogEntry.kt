package io.github.zerumi.is1_250924_12060.model

import java.io.Serializable
import java.time.ZonedDateTime

data class ImportLogEntry(
    val id: Long,
    val user: UserModel,
    val successful: Boolean,
    val importedCount: Int,
    val timestamp: ZonedDateTime,
) : Serializable

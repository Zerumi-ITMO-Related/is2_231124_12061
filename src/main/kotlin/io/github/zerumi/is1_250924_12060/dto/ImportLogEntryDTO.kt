package io.github.zerumi.is1_250924_12060.dto

import java.io.Serializable
import java.time.ZonedDateTime

data class ImportLogEntryDTO(
    val id: Long,
    val user: UserModelDTO,
    val successful: Boolean,
    val importedCount: Int,
    val timestamp: ZonedDateTime,
) : Serializable

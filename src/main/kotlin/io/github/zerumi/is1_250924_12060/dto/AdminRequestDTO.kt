package io.github.zerumi.is1_250924_12060.dto

import io.github.zerumi.is1_250924_12060.model.AdminRequestStatus
import java.io.Serializable
import java.time.ZonedDateTime

data class AdminRequestDTO(
    val id: Long,
    val user: UserModelDTO,
    val requestDate: ZonedDateTime,
    val status: AdminRequestStatus,
    val comment: String
) : Serializable

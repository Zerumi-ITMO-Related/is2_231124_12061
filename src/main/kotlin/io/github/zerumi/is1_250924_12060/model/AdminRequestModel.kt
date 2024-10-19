package io.github.zerumi.is1_250924_12060.model

import io.github.zerumi.is1_250924_12060.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToOne
import java.time.ZonedDateTime

data class AdminRequestModel(
    val id: Long = -1,
    val user: UserModel,
    val requestDate: ZonedDateTime,
    val status: AdminRequestStatus,
    val comment: String
)

enum class AdminRequestStatus() {
    PENDING,
    ACCEPTED,
    REJECTED
}

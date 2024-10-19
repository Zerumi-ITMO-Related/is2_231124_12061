package io.github.zerumi.is1_250924_12060.repository

import io.github.zerumi.is1_250924_12060.entity.AdminRequestEntity
import io.github.zerumi.is1_250924_12060.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRequestRepository : JpaRepository<AdminRequestEntity, Long> {
    fun findByUser(user: UserEntity): AdminRequestEntity?
}
package io.github.zerumi.is1_250924_12060.repository

import io.github.zerumi.is1_250924_12060.entity.ImportLogEntryEntity
import io.github.zerumi.is1_250924_12060.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ImportLogRepository : JpaRepository<ImportLogEntryEntity, Long> {
    fun findAllByUserEntity(userEntity: UserEntity): List<ImportLogEntryEntity>
}

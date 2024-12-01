package io.github.zerumi.is1_250924_12060.repository

import io.github.zerumi.is1_250924_12060.entity.CoordinatesEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CoordinatesRepository : JpaRepository<CoordinatesEntity, Long>

package io.github.zerumi.is1_250924_12060.dto

import java.io.Serializable

data class UserModelDTO(
    val id: Long,
    val username: String,
    val roles: List<String>,
) : Serializable

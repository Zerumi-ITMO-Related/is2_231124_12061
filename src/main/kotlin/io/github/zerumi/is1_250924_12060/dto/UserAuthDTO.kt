package io.github.zerumi.is1_250924_12060.dto

import java.io.Serializable

data class UserRequestDTO(
    val login: String,
    val password: String,
    val adminRequired: Boolean = false,
) : Serializable

data class AuthSessionResponseDTO(
    val token: String
) : Serializable

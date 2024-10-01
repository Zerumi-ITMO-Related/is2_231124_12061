package io.github.zerumi.is1_250924_12060.dto

data class UserRequestDTO(
    val login: String,
    val password: String
)

data class AuthSessionResponseDTO(
    val token: String
)

package io.github.zerumi.is1_250924_12060.dto

import java.io.Serializable

data class RestError(
    val status: String,
    val message: String
) : Serializable

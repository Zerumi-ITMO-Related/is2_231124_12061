package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.RestError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlerController {
    @ExceptionHandler(AuthenticationException::class)
    fun authException(ex: Exception): ResponseEntity<RestError> {
        val re = RestError(
            HttpStatus.UNAUTHORIZED.toString(),
            "Authentication failed at controller advice: ${ex.localizedMessage}"
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re)
    }

    @ExceptionHandler(Exception::class)
    fun generalException(ex: Exception): ResponseEntity<RestError> {
        val re = RestError(
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            "Something went wrong on server side: ${ex.localizedMessage}"
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re)
    }
}

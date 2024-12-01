package io.github.zerumi.is1_250924_12060.controller

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.github.zerumi.is1_250924_12060.dto.RestError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
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

    @ExceptionHandler(AccessDeniedException::class)
    fun notAnOwner(ex: Exception): ResponseEntity<RestError> {
        val re = RestError(
            HttpStatus.FORBIDDEN.toString(),
            "Access control check failed: ${ex.localizedMessage}"
        )
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(re)
    }

    @ExceptionHandler(InvalidFormatException::class)
    fun invalidData(ex: Exception): ResponseEntity<RestError> {
        val re = RestError(
            HttpStatus.BAD_REQUEST.toString(),
            "Input data is not valid: ${ex.localizedMessage}"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re)
    }

    @ExceptionHandler(MismatchedInputException::class)
    fun invalidFile(ex: Exception): ResponseEntity<RestError> {
        val re = RestError(
            HttpStatus.BAD_REQUEST.toString(),
            "File is not valid!"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re)
    }

    @ExceptionHandler(Exception::class)
    fun generalException(ex: Exception): ResponseEntity<RestError> {
        val re = RestError(
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            "Something went wrong on server side: ${ex.localizedMessage} / ${ex.javaClass.name}"
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re)
    }
}

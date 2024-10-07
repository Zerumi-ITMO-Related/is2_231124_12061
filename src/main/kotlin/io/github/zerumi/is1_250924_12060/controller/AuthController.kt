package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.AuthSessionResponseDTO
import io.github.zerumi.is1_250924_12060.dto.UserRequestDTO
import io.github.zerumi.is1_250924_12060.model.AuthSessionResponse
import io.github.zerumi.is1_250924_12060.model.UserRequest
import io.github.zerumi.is1_250924_12060.service.AuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class AuthController(
    val authService: AuthService,
) {
    @PostMapping("/login")
    fun login(@RequestBody dto: UserRequestDTO): ResponseEntity<AuthSessionResponseDTO> =
        ResponseEntity.ok(convertToDto(authService.login(convertToModel(dto))))

    @DeleteMapping("/logout")
    fun logout(@RequestHeader(HttpHeaders.AUTHORIZATION) authentication: String): ResponseEntity<*> {
        authService.logout(authentication)
        return ResponseEntity.noContent().build<Any>()
    }

    @PostMapping("/register")
    fun register(@RequestBody dto: UserRequestDTO): ResponseEntity<AuthSessionResponseDTO> {
        authService.register(convertToModel(dto))
        return login(dto)
    }

    private fun convertToModel(dto: UserRequestDTO): UserRequest = UserRequest(dto.login, dto.password)

    private fun convertToDto(authSessionResponse: AuthSessionResponse): AuthSessionResponseDTO = AuthSessionResponseDTO(
        token = authSessionResponse.token
    )
}

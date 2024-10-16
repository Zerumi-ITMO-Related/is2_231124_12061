package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.UserModelDTO
import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.service.SessionHandler
import io.github.zerumi.is1_250924_12060.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/whoami")
class WhoAmIController(val sessionHandler: SessionHandler, val userService: UserService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun whoAmI(
        @RequestHeader(HttpHeaders.AUTHORIZATION) auth: String
    ): UserModelDTO = toDTO(
        userService.loadUserByUsername(sessionHandler.getUsernameForSession(auth)!!)
    )

    fun toDTO(user: UserModel): UserModelDTO = UserModelDTO(
        id = user.id,
        username = user.username,
        roles = user.authorities.map { it.authority ?: "" }
    )
}
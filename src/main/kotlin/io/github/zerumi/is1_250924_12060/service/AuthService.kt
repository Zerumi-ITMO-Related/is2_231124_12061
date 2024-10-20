package io.github.zerumi.is1_250924_12060.service

import io.github.zerumi.is1_250924_12060.configuration.SecurityConfig
import io.github.zerumi.is1_250924_12060.entity.RoleEntity
import io.github.zerumi.is1_250924_12060.entity.UserEntity
import io.github.zerumi.is1_250924_12060.model.AuthSessionResponse
import io.github.zerumi.is1_250924_12060.model.UserRequest
import io.github.zerumi.is1_250924_12060.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class AuthService(
    private val userService: UserService,
    private val adminService: AdminService,
    private val securityConfig: SecurityConfig,
    private val authenticationManager: AuthenticationManager,
    private val handler: SessionHandler,
    private val repository: UserRepository,
) {
    fun login(userRequest: UserRequest): AuthSessionResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                userRequest.login, userRequest.password
            )
        )

        val sessionID: String = handler.register(userRequest.login)
        val response = AuthSessionResponse(sessionID)
        return response
    }

    fun logout(authentication: String) =
        handler.invalidate(authentication)

    fun register(userRequest: UserRequest) {
        val username = userRequest.login
        val encryptedPass = securityConfig.passwordEncoder().encode(userRequest.password)

        val unluckyUser = repository.findAll().first {
            securityConfig.passwordEncoder().matches(userRequest.password, it.password)
        } // FR!!! Password should be unique

        if (repository.findByUsername(username) != null)
            throw BadCredentialsException("User with that username already exists")
        if (unluckyUser != null)
            throw BadCredentialsException("User ${unluckyUser.username} has already taken password that you provide. " +
                    "Try to use another password.")

        val newUser = UserEntity(
            username = username,
            password = encryptedPass,
            roles = emptyList<RoleEntity>().toMutableList()
        )

        if (userRequest.adminRequired) {
            adminService.newAdminRequest(userService.convertEntityToModel(newUser))
        } else {
            repository.save(newUser)
        }
    }
}

@Component
class SessionHandler {
    companion object {
        private val sessions = HashMap<String, String>()
    }

    fun register(username: String): String {
        val sessionID = generateSessionID()
        sessions[sessionID] = username
        return sessionID
    }

    fun getUsernameForSession(sessionID: String): String? = sessions[sessionID]

    private fun generateSessionID(): String {
        return String(
            Base64.getEncoder().encode(
                UUID.randomUUID().toString().toByteArray(StandardCharsets.UTF_8)
            )
        )
    }

    fun invalidate(token: String) = sessions.remove(token)
}

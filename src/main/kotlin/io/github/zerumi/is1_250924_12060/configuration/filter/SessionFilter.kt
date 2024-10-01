package io.github.zerumi.is1_250924_12060.configuration.filter

import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.service.UserService
import io.github.zerumi.is1_250924_12060.service.SessionHandler
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SessionFilter(
    private val handler: SessionHandler,
    private val userService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val sessionId = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (sessionId == null || sessionId.isEmpty()) {
            filterChain.doFilter(request, response)
            return
        }

        val username = handler.getUsernameForSession(sessionId)
        if (username == null) {
            filterChain.doFilter(request, response)
            return
        }

        val user: UserModel = userService.loadUserByUsername(username)
        val auth = UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities()
        )
        auth.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = auth
        filterChain.doFilter(request, response)
    }
}

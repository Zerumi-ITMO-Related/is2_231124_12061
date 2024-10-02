package io.github.zerumi.is1_250924_12060.configuration

import io.github.zerumi.is1_250924_12060.service.SessionHandler
import io.github.zerumi.is1_250924_12060.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
class WebSocketConfig(
    val authChannelInterceptorAdapter: AuthChannelInterceptorAdapter
) : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/ws-api")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/socket").setAllowedOrigins("http://localhost:4200").withSockJS()
        registry.addEndpoint("/socket").setAllowedOrigins("http://localhost:4200")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(authChannelInterceptorAdapter)
    }

    // ВОСХИТИТЕЛЬНЫЙ КОСТЫЛЬ
    @Bean("csrfChannelInterceptor")
    fun noopCsrfChannelInterceptor(): ChannelInterceptor {
        return object : ChannelInterceptor {}
    }
}

@Component
class AuthChannelInterceptorAdapter(
    private val handler: SessionHandler, private val userService: UserService
) : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor: StompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
            ?: throw BadCredentialsException("")

        if (StompCommand.CONNECT == accessor.command) {
            val token = accessor.getFirstNativeHeader("authorization") ?: throw BadCredentialsException("")
            val username = handler.getUsernameForSession(token) ?: throw BadCredentialsException("")
            val details = userService.loadUserByUsername(username)

            val user: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                details.username, details.password, details.getAuthorities()
            )

            SecurityContextHolder.getContext().authentication = user
            accessor.user = user
        }
        return message
    }
}

package io.github.zerumi.is1_250924_12060.configuration

import io.github.zerumi.is1_250924_12060.configuration.filter.SessionFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authorization.AuthorityAuthorizationManager
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableWebSecurity
@EnableWebSocketSecurity
class SecurityConfig(
    val sessionFilter: SessionFilter, private val mvc: MvcRequestMatcher.Builder
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors { it.disable() }.csrf { it.disable() }
        http.csrf { csrf ->
            csrf.ignoringRequestMatchers("/**")
        }
        http.headers{ headersConfigurer -> headersConfigurer.frameOptions { it.sameOrigin() } }

        val config = CorsConfiguration()

        config.allowCredentials = true
        config.addAllowedOrigin("http://localhost:4200")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        val corsFilter = CorsFilter(source)

        http.exceptionHandling { eh: ExceptionHandlingConfigurer<HttpSecurity?> ->
            eh.authenticationEntryPoint { rq: HttpServletRequest?, rs: HttpServletResponse, ex: AuthenticationException ->
                rs.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, ex.localizedMessage
                )
            }
        }
        http.authorizeHttpRequests { ar ->
            ar.requestMatchers(mvc.pattern("/api/v1/login")).permitAll()
            ar.requestMatchers(mvc.pattern("/api/v1/register")).permitAll()
            ar.requestMatchers(mvc.pattern("/socket")).permitAll()
            ar.anyRequest().authenticated()
        }.httpBasic(Customizer.withDefaults())

        http.addFilter(corsFilter)
        http.addFilterAfter(sessionFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun messageAuthorizationManager(messages: MessageMatcherDelegatingAuthorizationManager.Builder): AuthorizationManager<Message<*>>? {
        messages.simpTypeMatchers(SimpMessageType.CONNECT,
            SimpMessageType.HEARTBEAT,
            SimpMessageType.UNSUBSCRIBE,
            SimpMessageType.DISCONNECT).permitAll()
            .anyMessage().authenticated()

        return messages.build();
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}

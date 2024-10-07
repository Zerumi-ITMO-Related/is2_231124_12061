package io.github.zerumi.is1_250924_12060.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
class AppConfig {
    @Bean
    fun mvc(introspector: HandlerMappingIntrospector?): MvcRequestMatcher.Builder {
        return MvcRequestMatcher.Builder(introspector)
    }
}

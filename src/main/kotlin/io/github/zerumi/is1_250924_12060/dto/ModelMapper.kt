package io.github.zerumi.is1_250924_12060.dto

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Bean
fun modelMapper(): ModelMapper {
    return ModelMapper()
}

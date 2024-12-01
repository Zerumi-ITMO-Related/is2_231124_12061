package io.github.zerumi.is1_250924_12060.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.zerumi.is1_250924_12060.dto.HumanBeingDTO
import io.github.zerumi.is1_250924_12060.repository.ModelRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class MassModelImportService(
    val modelRepository: ModelRepository,
) {
    fun processInput(file: MultipartFile): List<HumanBeingDTO> {
        return decodeHumanBeingsFromYaml(file.inputStream)
    }

    fun decodeHumanBeingsFromYaml(inputStream: InputStream): List<HumanBeingDTO> {
        val objectMapper = ObjectMapper(YAMLFactory()).registerModule(
            KotlinModule.Builder()
                .configure(KotlinFeature.NullToEmptyCollection, true)
                .configure(KotlinFeature.NullToEmptyMap, true)
                .configure(KotlinFeature.NullIsSameAsDefault, true)
                .configure(KotlinFeature.SingletonSupport, true)
                .configure(KotlinFeature.StrictNullChecks, true)
                .build()
        )
        return objectMapper.readValue(inputStream)
    }
}
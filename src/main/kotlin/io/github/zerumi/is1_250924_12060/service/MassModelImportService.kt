package io.github.zerumi.is1_250924_12060.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.zerumi.is1_250924_12060.controller.ModelController
import io.github.zerumi.is1_250924_12060.dto.HumanBeingDTO
import io.github.zerumi.is1_250924_12060.dto.HumanBeingFullDTO
import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.repository.ModelRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class MassModelImportService(
    val modelController: ModelController,
    val modelService: ModelService,
    val modelRepository: ModelRepository
) {
    @Transactional
    fun processInput(file: MultipartFile, user: UserModel): List<HumanBeingFullDTO> {
        val dtos = decodeHumanBeingsFromYaml(file.inputStream)
        val entities = dtos.map { modelController.convertToModel(it, user) }.map { modelService.convertToEntity(it) }

        val result = modelRepository.saveAll(entities)
        return result.map { modelService.convertToModel(it) }.map { modelController.convertToDto(it) }
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
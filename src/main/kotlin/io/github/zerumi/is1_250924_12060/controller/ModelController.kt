package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.HumanBeingDTO
import io.github.zerumi.is1_250924_12060.model.HumanBeing
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/model")
class ModelController(
    val modelMapper: ModelMapper
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getModelByID(@PathVariable id: Long): HumanBeingDTO {
        TODO("Not yet implemented")
        //return convertToDto
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createModel(@RequestBody dto: HumanBeingDTO) {
        val entity = convertToEntity(dto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateModel(@PathVariable id: Long, @RequestBody dto: HumanBeingDTO) {
        val entity = convertToEntity(dto)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteModel(@PathVariable id: Long) {

    }

    fun convertToDto(model: HumanBeing): HumanBeingDTO =
        modelMapper.map(model, HumanBeingDTO::class.java)

    fun convertToEntity(dto: HumanBeingDTO): HumanBeing =
        modelMapper.map(dto, HumanBeing::class.java)
}
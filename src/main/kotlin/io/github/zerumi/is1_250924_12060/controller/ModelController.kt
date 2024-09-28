package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.CarDTO
import io.github.zerumi.is1_250924_12060.dto.CoordinatesDTO
import io.github.zerumi.is1_250924_12060.dto.HumanBeingDTO
import io.github.zerumi.is1_250924_12060.model.Car
import io.github.zerumi.is1_250924_12060.model.Coordinates
import io.github.zerumi.is1_250924_12060.model.HumanBeing
import io.github.zerumi.is1_250924_12060.service.ModelService
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
    val modelService: ModelService
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getModelByID(@PathVariable id: Long): HumanBeingDTO {
        return convertToDto(modelService.getById(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createModel(@RequestBody dto: HumanBeingDTO) {
        val entity = convertToEntity(dto)
        modelService.create(entity)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateModel(@PathVariable id: Long, @RequestBody dto: HumanBeingDTO) {
        val entity = convertToEntity(dto)
        modelService.updateById(id, entity)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteModel(@PathVariable id: Long) {
        modelService.deleteById(id)
    }

    fun convertToDto(model: HumanBeing): HumanBeingDTO = HumanBeingDTO(
        name = model.name,
        coordinates = CoordinatesDTO(
            x = model.coordinates.x,
            y = model.coordinates.y
        ),
        realHero = model.realHero,
        hasToothpick = model.hasToothpick,
        car = CarDTO(
            model.car.name
        ),
        mood = model.mood,
        impactSpeed = model.impactSpeed,
        minutesOfWaiting = model.minutesOfWaiting,
        weaponType = model.weaponType
    )

    fun convertToEntity(dto: HumanBeingDTO): HumanBeing = HumanBeing(
        name = dto.name,
        coordinates = Coordinates(
            x = dto.coordinates.x,
            y = dto.coordinates.y
        ),
        realHero = dto.realHero,
        hasToothpick = dto.hasToothpick,
        car = Car(
            name = dto.car.name
        ),
        mood = dto.mood,
        impactSpeed = dto.impactSpeed,
        minutesOfWaiting = dto.minutesOfWaiting,
        weaponType = dto.weaponType
    )
}
package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.CarDTO
import io.github.zerumi.is1_250924_12060.dto.CoordinatesDTO
import io.github.zerumi.is1_250924_12060.dto.HumanBeingDTO
import io.github.zerumi.is1_250924_12060.dto.HumanBeingFullDTO
import io.github.zerumi.is1_250924_12060.model.Car
import io.github.zerumi.is1_250924_12060.model.Coordinates
import io.github.zerumi.is1_250924_12060.model.HumanBeing
import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.service.ModelService
import io.github.zerumi.is1_250924_12060.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/model")
class ModelController(
    val modelService: ModelService,
    val userService: UserService,
    val simpMessagingTemplate: SimpMessagingTemplate,
) {
    @GetMapping
    fun getAllModels(): List<HumanBeingFullDTO> {
        return modelService.getAll().map { convertToDto(it) }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getModelByID(@PathVariable id: Long): HumanBeingFullDTO {
        return convertToDto(modelService.getById(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createModel(@RequestBody dto: HumanBeingDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) auth: String) {
        val entity = convertToModel(dto, userService.loadUserBySessionId(auth))
        val saved = modelService.create(entity)
        simpMessagingTemplate.convertAndSend("/topic/newModel", saved)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateModel(@PathVariable id: Long, @RequestBody dto: HumanBeingDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) auth: String) {
        val entity = convertToModel(dto, userService.loadUserBySessionId(auth))
        val updated = modelService.updateById(id, entity, userService.loadUserBySessionId(auth))
        simpMessagingTemplate.convertAndSend("/topic/updatedModel", object {
            val id = id
            val modelDto = convertToDto(updated)
        })
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteModel(@PathVariable id: Long, @RequestHeader(HttpHeaders.AUTHORIZATION) auth: String) {
        modelService.deleteById(id, userService.loadUserBySessionId(auth))
        simpMessagingTemplate.convertAndSend("/topic/removeModel", id)
    }

    @DeleteMapping("/deleteByWeaponType")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteHumanBeingByWeaponType(weaponType: String) {
        simpMessagingTemplate.convertAndSend("/topic/invalidatedModel")
        modelService.deleteHumanBeingByWeaponType(weaponType)
    }

    @GetMapping("/totalMinutesOfWaiting")
    @ResponseStatus(HttpStatus.OK)
    fun calculateTotalMinutesOfWaiting(): Long {
        return modelService.calculateTotalMinutesOfWaiting()
    }

    @DeleteMapping("/deleteWoToothpick")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteHeroesWithoutToothpick() {
        simpMessagingTemplate.convertAndSend("/topic/invalidatedModel")
        modelService.deleteHeroesWithoutToothpick()
    }

    @PatchMapping("/makeThemAllSad")
    @ResponseStatus(HttpStatus.OK)
    fun updateAllHeroesToSadMood() {
        simpMessagingTemplate.convertAndSend("/topic/invalidatedModel")
        modelService.updateAllHeroesToSadMood()
    }

    fun convertToDto(model: HumanBeing): HumanBeingFullDTO = HumanBeingFullDTO(
        id = model.id,
        name = model.name,
        coordinates = CoordinatesDTO(
            x = model.coordinates.x,
            y = model.coordinates.y
        ),
        creationDate = model.creationDate,
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

    fun convertToModel(dto: HumanBeingDTO, owner: UserModel): HumanBeing = HumanBeing(
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
        weaponType = dto.weaponType,
        owner = owner
    )
}

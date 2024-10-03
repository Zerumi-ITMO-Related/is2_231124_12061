package io.github.zerumi.is1_250924_12060.service

import io.github.zerumi.is1_250924_12060.entity.CarEntity
import io.github.zerumi.is1_250924_12060.entity.CoordinatesEntity
import io.github.zerumi.is1_250924_12060.entity.HumanBeingEntity
import io.github.zerumi.is1_250924_12060.model.Car
import io.github.zerumi.is1_250924_12060.model.Coordinates
import io.github.zerumi.is1_250924_12060.model.HumanBeing
import io.github.zerumi.is1_250924_12060.repository.ModelRepository
import org.springframework.stereotype.Service

@Service
class ModelService(
    val modelRepository: ModelRepository
) {
    fun create(model: HumanBeing): HumanBeing {
        val entity = convertToEntity(model)
        val saved = modelRepository.save(entity)
        return convertToModel(saved)
    }

    fun getById(id: Long) : HumanBeing {
        val entity = modelRepository.getReferenceById(id)
        return convertToModel(entity)
    }

    fun updateById(id: Long, model: HumanBeing): HumanBeing {
        val newEntity = convertToEntity(model)
        newEntity.id = id
        modelRepository.deleteById(id)
        val saved = modelRepository.save(newEntity)
        return convertToModel(saved)
    }

    fun deleteById(id: Long) {
        modelRepository.deleteById(id)
    }

    fun convertToEntity(model: HumanBeing): HumanBeingEntity = HumanBeingEntity(
        name = model.name,
        coordinates = CoordinatesEntity(
            x = model.coordinates.x,
            y = model.coordinates.y,
        ),
        creationDate = model.creationDate,
        realHero = model.realHero,
        hasToothpick = model.hasToothpick,
        car = CarEntity(
            name = model.car.name
        ),
        mood = model.mood,
        impactSpeed = model.impactSpeed,
        minutesOfWaiting = model.minutesOfWaiting,
        weaponType = model.weaponType
    )
        
    fun convertToModel(entity: HumanBeingEntity): HumanBeing = HumanBeing(
        id = entity.id ?: -1,
        name = entity.name,
        coordinates = Coordinates(
            x = entity.coordinates.x,
            y = entity.coordinates.y
        ),
        realHero = entity.realHero,
        hasToothpick = entity.hasToothpick,
        car = Car(
            entity.car.name
        ),
        mood = entity.mood,
        impactSpeed = entity.impactSpeed,
        minutesOfWaiting = entity.minutesOfWaiting,
        weaponType = entity.weaponType,
    )

    fun getAll(): List<HumanBeing> = modelRepository.findAll().map { convertToModel(it) }
}

package io.github.zerumi.is1_250924_12060.service

import io.github.zerumi.is1_250924_12060.entity.CarEntity
import io.github.zerumi.is1_250924_12060.entity.CoordinatesEntity
import io.github.zerumi.is1_250924_12060.entity.HumanBeingEntity
import io.github.zerumi.is1_250924_12060.model.Car
import io.github.zerumi.is1_250924_12060.model.Coordinates
import io.github.zerumi.is1_250924_12060.model.HumanBeing
import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.repository.ModelRepository
import io.github.zerumi.is1_250924_12060.repository.UserRepository
import io.github.zerumi.is1_250924_12060.validation.ModelValidator
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ModelService(
    val modelRepository: ModelRepository,
    val userRepository: UserRepository,
    val modelValidator: ModelValidator,
) {
    @Transactional
    fun create(model: HumanBeing): HumanBeing {
        val entity = convertToEntity(model)
        if (modelValidator.validateModel(entity)) {
            val saved = modelRepository.save(entity)
            return convertToModel(saved)
        } else throw IllegalArgumentException("Unique coordinates count exceeded!")
    }

    fun getById(id: Long) : HumanBeing {
        val entity = modelRepository.getReferenceById(id)
        return convertToModel(entity)
    }

    fun deleteHumanBeingByWeaponType(weaponType: String) = modelRepository.deleteHumanBeingByWeaponType(weaponType)

    fun calculateTotalMinutesOfWaiting(): Long = modelRepository.calculateTotalMinutesOfWaiting()

    fun deleteHeroesWithoutToothpick() = modelRepository.deleteHeroesWithoutToothpick()

    fun updateAllHeroesToSadMood() = modelRepository.updateAllHeroesToSadMood()

    @Transactional
    fun updateById(id: Long, model: HumanBeing, user: UserModel): HumanBeing {
        if (convertToModel(modelRepository.getReferenceById(id)).owner != user
            && user.authorities.find { it.authority == "ADMIN" } == null)
            throw AccessDeniedException("You are not owner of the object")
        val newEntity = convertToEntity(model)
        val jpaModel = modelRepository.getReferenceById(id)
        jpaModel.name = newEntity.name
        jpaModel.car = newEntity.car
        jpaModel.mood = newEntity.mood
        jpaModel.coordinates = newEntity.coordinates
        jpaModel.hasToothpick = newEntity.hasToothpick
        jpaModel.impactSpeed = newEntity.impactSpeed
        jpaModel.minutesOfWaiting = newEntity.minutesOfWaiting
        jpaModel.realHero = newEntity.realHero
        jpaModel.weaponType = newEntity.weaponType
        val saved = modelRepository.save(jpaModel)
        return convertToModel(saved)
    }

    fun deleteById(id: Long, user: UserModel) {
        if (convertToModel(modelRepository.getReferenceById(id)).owner == user
            || user.authorities.find { it.authority == "ADMIN" } != null)
            modelRepository.deleteById(id)
        else throw AccessDeniedException("You are not owner of the object")
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
        weaponType = model.weaponType,
        owner = userRepository.getReferenceById(model.owner.id)
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
        owner = UserModel(
            id = entity.owner.id ?: -1,
            username = entity.owner.username,
            password = entity.owner.password,
            accountNonExpired = entity.owner.isAccountNonExpired ?: true,
            accountNonLocked = entity.owner.isAccountNonLocked ?: true,
            credentialsNonExpired = entity.owner.isCredentialsNonExpired ?: true,
            enabled = entity.owner.isEnabled ?: true,
            roles = entity.owner.roles.map { it.roleName }
        )
    )

    fun getAll(): List<HumanBeing> = modelRepository.findAll().map { convertToModel(it) }
}

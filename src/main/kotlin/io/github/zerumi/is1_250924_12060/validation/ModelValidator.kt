package io.github.zerumi.is1_250924_12060.validation

import io.github.zerumi.is1_250924_12060.entity.HumanBeingEntity
import io.github.zerumi.is1_250924_12060.repository.CoordinatesRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class ModelValidator(
    val coordinatesRepository: CoordinatesRepository
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    fun validateModel(model: HumanBeingEntity, alreadyRepeat: Int = 0): Boolean {
        val allCoordinates = coordinatesRepository.findAll()
        return allCoordinates.count { it.x == model.coordinates.x && it.y == model.coordinates.y } < 10 - alreadyRepeat
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    fun validateModel(models: List<HumanBeingEntity>): Boolean {
        val repeat = models.groupingBy { it.coordinates }.eachCount()
        return models.all { validateModel(it, repeat[it.coordinates]!!) }
    }
}
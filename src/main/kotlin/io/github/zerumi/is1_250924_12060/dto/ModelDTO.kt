package io.github.zerumi.is1_250924_12060.dto

import io.github.zerumi.is1_250924_12060.model.Mood
import io.github.zerumi.is1_250924_12060.model.WeaponType
import java.io.Serializable

data class HumanBeingDTO(
    val name: String,
    val coordinates: CoordinatesDTO,
    val realHero: Boolean,
    val hasToothpick: Boolean?,
    val car: CarDTO,
    val mood: Mood,
    val impactSpeed: Long,
    val minutesOfWaiting: Int,
    val weaponType: WeaponType,
): Serializable

data class CoordinatesDTO(
    val x: Long,
    val y: Long,
) : Serializable

data class CarDTO(
    val name: String?
) : Serializable

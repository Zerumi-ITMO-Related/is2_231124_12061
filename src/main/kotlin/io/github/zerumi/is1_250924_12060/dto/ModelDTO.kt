package io.github.zerumi.is1_250924_12060.dto

data class HumanBeingDTO(
    val name: String,
    val coordinates: CoordinatesDTO,
    val realHero: Boolean,
    val hasToothpick: Boolean?,
    val car: CarDTO,
    val mood: MoodDTO,
    val impactSpeed: Long,
    val minutesOfWaiting: Int,
    val weaponType: WeaponTypeDTO,
)

data class CoordinatesDTO(
    val x: Long,
    val y: Long,
)

data class CarDTO(
    val name: String?
)

enum class MoodDTO {
    SADNESS, LONGING, GLOOM, RAGE, FRENZY
}

enum class WeaponTypeDTO {
    AXE, PISTOL, KNIFE, BAT
}

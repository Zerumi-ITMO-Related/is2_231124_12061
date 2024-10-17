package io.github.zerumi.is1_250924_12060.model

import java.time.ZonedDateTime

data class HumanBeing(
    val id: Long = -1,                  // Значение поля должно быть больше 0
                                        // Значение этого поля должно быть уникальным
                                        // Значение этого поля должно генерироваться автоматически
    val name: String,                   // Поле не может быть null, Строка не может быть пустой
    val coordinates: Coordinates,       // Поле не может быть null
    val realHero: Boolean,              // Поле не может быть null
    val hasToothpick: Boolean?,         // Поле может быть null
    val car: Car,                       // Поле не может быть null
    val mood: Mood,                     // Поле не может быть null
    val impactSpeed: Long,              // Поле не может быть null
    val minutesOfWaiting: Int,          // << no additional requirements >>
    val weaponType: WeaponType,         // Поле не может быть null
    val owner: UserModel,
) {
    init {
        require(name.isNotBlank()) { "Name parameter shouldn't be blank or empty!" }
    }

    val creationDate: ZonedDateTime = ZonedDateTime.now()   // Поле не может быть null,
    // Значение этого поля должно генерироваться автоматически
}

data class Coordinates(
    val x: Long,                        // Максимальное значение поля: 605, Поле не может быть null
    val y: Long,                        // Поле не может быть null
) {
    init {
        require(x <= 605) { "Parameter X should be less or 605, got: $x" }
    }
}

data class Car(
    val name: String?                   // Поле может быть null
)

enum class Mood {
    SADNESS, LONGING, GLOOM, RAGE, FRENZY
}

enum class WeaponType {
    AXE, PISTOL, KNIFE, BAT
}

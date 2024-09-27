package io.github.zerumi.is1_250924_12060.model

import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicLong

data class HumanBeing(
    val name: String,                   // Поле не может быть null, Строка не может быть пустой
    val coordinates: Coordinates,       // Поле не может быть null
    val realHero: Boolean,              // Поле не может быть null
    val hasToothpick: Boolean?,         // Поле может быть null
    val car: Car,                       // Поле не может быть null
    val mood: Mood,                     // Поле не может быть null
    val impactSpeed: Long,              // Поле не может быть null
    val minutesOfWaiting: Int,          // << no additional requirements >>
    val weaponType: WeaponType,         // Поле не может быть null
) {
    companion object {
        private var next_id: AtomicLong = AtomicLong(0)
        fun generateNextId() = next_id.getAndIncrement()
    }

    init {
        require(name.isNotBlank()) { "Name parameter shouldn't be blank or empty!" }
    }

    var id: Long = generateNextId()    // Значение поля должно быть больше 0,
        private set
    // Значение этого поля должно быть уникальным,
    // Значение этого поля должно генерироваться автоматически

    val creationDate: ZonedDateTime = ZonedDateTime.now()   // Поле не может быть null,
    // Значение этого поля должно генерироваться автоматически

    /**
     * This function returns modified object.
     * Unlike `copy()`, this function saves ID of original object
     * This function doesn't affect on ID generation
     *
     * @since 1.0
     * @author Zerumi
     */
    fun modified(
        name: String = this.name,
        coordinates: Coordinates = this.coordinates,
        realHero: Boolean = this.realHero,
        hasToothpick: Boolean? = this.hasToothpick,
        car: Car = this.car,
        mood: Mood = this.mood,
        impactSpeed: Long = this.impactSpeed,
        minutesOfWaiting: Int = this.minutesOfWaiting,
        weaponType: WeaponType = this.weaponType
    ): HumanBeing {
        val copy = this.copy(
            name = name,
            coordinates = coordinates,
            realHero = realHero,
            hasToothpick = hasToothpick,
            car = car,
            mood = mood,
            impactSpeed = impactSpeed,
            minutesOfWaiting = minutesOfWaiting,
            weaponType = weaponType
        )
        next_id.decrementAndGet()
        copy.id = this.id
        return copy
    }
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

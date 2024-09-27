package io.github.zerumi.is1_250924_12060.entity

import io.github.zerumi.is1_250924_12060.model.Mood
import io.github.zerumi.is1_250924_12060.model.WeaponType
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "human_being")
class ModelEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    var name: String,
    var coordinates: CoordinatesEntity,
    var creationDate: ZonedDateTime,
    var realHero: Boolean,
    var hasToothpick: Boolean?,
    var car: CarEntity,
    @Enumerated(EnumType.STRING)
    var mood: Mood,
    var impactSpeed: Long,
    var minutesOfWaiting: Int,
    @Enumerated(EnumType.STRING)
    var weaponType: WeaponType,
)

@Entity
@Table(name = "human_being_coordinates")
class CoordinatesEntity(
    var x: Long,
    var y: Long
)

@Entity
@Table(name = "human_being_car")
class CarEntity(
    var name: String?
)

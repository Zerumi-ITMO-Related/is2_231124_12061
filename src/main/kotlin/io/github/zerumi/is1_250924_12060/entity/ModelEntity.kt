package io.github.zerumi.is1_250924_12060.entity

import io.github.zerumi.is1_250924_12060.model.Mood
import io.github.zerumi.is1_250924_12060.model.WeaponType
import jakarta.persistence.*
import java.io.Serializable
import java.time.ZonedDateTime

@Entity
@Table(name = "human_being")
class HumanBeingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,
    var name: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "coordinates_id", unique = true, nullable = false)
    var coordinates: CoordinatesEntity,
    var creationDate: ZonedDateTime,
    var realHero: Boolean,
    var hasToothpick: Boolean?,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "car_id", unique = true, nullable = false)
    var car: CarEntity,
    @Enumerated(EnumType.STRING)
    var mood: Mood,
    var impactSpeed: Long,
    var minutesOfWaiting: Int,
    @Enumerated(EnumType.STRING)
    var weaponType: WeaponType,
    @OneToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "owner_id", unique = false, nullable = false)
    var owner: UserEntity
) : Serializable

@Entity
@Table(name = "human_being_coordinates")
class CoordinatesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,
    var x: Long,
    var y: Long
) : Serializable

@Entity
@Table(name = "human_being_car")
class CarEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,
    var name: String?
) : Serializable

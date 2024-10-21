package io.github.zerumi.is1_250924_12060.repository

import io.github.zerumi.is1_250924_12060.entity.HumanBeingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ModelRepository : JpaRepository<HumanBeingEntity, Long> {
    @Query(
        value = "select delete_human_being_by_weapon_type(:p_weapon_type)",
        nativeQuery = true
    )
    fun deleteHumanBeingByWeaponType(@Param("p_weapon_type") weaponType: String)

    @Query(
        value = "select calculate_total_minutes_of_waiting()",
        nativeQuery = true
    )
    fun calculateTotalMinutesOfWaiting(): Long

    @Query(
        value = "select delete_heroes_without_toothpick()",
        nativeQuery = true
    )
    fun deleteHeroesWithoutToothpick()

    @Query(
        value = "select update_all_heroes_to_sad_mood()",
        nativeQuery = true
    )
    fun updateAllHeroesToSadMood()
}

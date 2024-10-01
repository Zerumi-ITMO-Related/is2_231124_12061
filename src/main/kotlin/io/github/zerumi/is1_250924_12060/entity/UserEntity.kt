package io.github.zerumi.is1_250924_12060.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "user_lab4", schema = "s367837")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var username: String,
    @Column(nullable = false)
    var password: String,
    @Column(name = "acc_non_expired")
    var isAccountNonExpired: Boolean? = true,
    @Column(name = "non_locked")
    var isAccountNonLocked: Boolean? = true,
    @Column(name = "crd_non_expired")
    var isCredentialsNonExpired: Boolean? = true,
    @Column
    var isEnabled: Boolean? = true
) : Serializable

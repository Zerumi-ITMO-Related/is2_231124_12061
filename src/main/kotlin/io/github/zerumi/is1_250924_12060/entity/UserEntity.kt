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
    @Column(name = "enabled")
    var isEnabled: Boolean? = true,
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(name = "roles_lab4_entities", joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableList<RoleEntity>
) : Serializable

@Entity
@Table(name = "roles_lab4", schema = "s367837")
class RoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var roleId: Long? = null,
    var roleName: String,
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(name = "roles_lab4_entities", joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableList<UserEntity>
) : Serializable

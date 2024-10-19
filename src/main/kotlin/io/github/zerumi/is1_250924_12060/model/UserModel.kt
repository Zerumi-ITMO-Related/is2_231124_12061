package io.github.zerumi.is1_250924_12060.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserModel(
    val id: Long,
    private val username: String,
    private val password: String,
    private val accountNonExpired: Boolean = true,
    private val accountNonLocked: Boolean = true,
    private val credentialsNonExpired: Boolean = true,
    private val enabled: Boolean = true,
    private val roles: List<String>,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        roles.map { SimpleGrantedAuthority(it) }.plus(SimpleGrantedAuthority("USER")).toMutableList()

    override fun getUsername(): String = username
    override fun getPassword(): String = password
    override fun isAccountNonExpired(): Boolean = accountNonExpired
    override fun isAccountNonLocked(): Boolean = accountNonLocked
    override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired
    override fun isEnabled(): Boolean = enabled
}

data class UserRequest(
    val login: String, val password: String, val adminRequired: Boolean = false
)

data class AuthSessionResponse(
    val token: String
)

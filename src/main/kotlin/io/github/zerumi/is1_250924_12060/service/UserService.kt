package io.github.zerumi.is1_250924_12060.service

import io.github.zerumi.is1_250924_12060.entity.UserEntity
import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService (val repository: UserRepository, val sessionHandler: SessionHandler) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserModel {
        val foundUser: UserEntity = repository.findByUsername(username)
            ?: throw UsernameNotFoundException("Cannot find user with name $username")

        return convertEntityToModel(foundUser)
    }

    fun convertEntityToModel(entity: UserEntity): UserModel = UserModel(
        id = entity.id ?: -1,
        username = entity.username,
        password = entity.password,
        accountNonExpired = entity.isAccountNonExpired ?: true,
        accountNonLocked = entity.isAccountNonLocked ?: true,
        credentialsNonExpired = entity.isCredentialsNonExpired ?: true,
        enabled = entity.isEnabled ?: true
    )

    fun loadUserBySessionId(auth: String): UserModel = loadUserByUsername(sessionHandler.getUsernameForSession(auth)!!)
}

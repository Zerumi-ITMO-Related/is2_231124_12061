package io.github.zerumi.is1_250924_12060.service

import io.github.zerumi.is1_250924_12060.entity.AdminRequestEntity
import io.github.zerumi.is1_250924_12060.entity.UserEntity
import io.github.zerumi.is1_250924_12060.model.AdminRequestModel
import io.github.zerumi.is1_250924_12060.model.AdminRequestStatus
import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.repository.AdminRequestRepository
import io.github.zerumi.is1_250924_12060.repository.RoleRepository
import io.github.zerumi.is1_250924_12060.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class AdminService(
    val adminRequestRepository: AdminRequestRepository,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository
) {
    fun getAllRequests(): List<AdminRequestModel> =
        adminRequestRepository.findAll().mapNotNull { convertToModel(it) }.toList()

    fun newAdminRequest(user: UserModel) {
        val request = makeAdminRequest(convertToEntity(user))
        adminRequestRepository.save(request)
    }

    @Transactional
    fun approveAdminRequest(requestModel: AdminRequestModel, comment: String = "Your request successfully approved") {
        val request = adminRequestRepository.getReferenceById(requestModel.id)

        val adminRole = roleRepository.findByRoleName("ADMIN")!!
        request.user.roles.add(adminRole)

        request.status = AdminRequestStatus.ACCEPTED
        request.comment = comment

        adminRequestRepository.save(request)
    }

    @Transactional
    fun declineAdminRequest(requestModel: AdminRequestModel, comment: String = "Your request was rejected") {
        val request = adminRequestRepository.getReferenceById(requestModel.id)

        request.status = AdminRequestStatus.REJECTED
        request.comment = comment

        adminRequestRepository.save(request)
    }

    private fun makeAdminRequest(user: UserEntity) = AdminRequestEntity(
        user = user,
        requestDate = ZonedDateTime.now(),
        status = AdminRequestStatus.PENDING,
        comment = "Request sent"
    )

    private fun convertToModel(adminRequestEntity: AdminRequestEntity): AdminRequestModel = AdminRequestModel(
        id = adminRequestEntity.id ?: -1,
        user = UserModel(
            id = adminRequestEntity.user.id ?: -1,
            username = adminRequestEntity.user.username,
            password = adminRequestEntity.user.username,
            accountNonExpired = adminRequestEntity.user.isAccountNonExpired ?: true,
            accountNonLocked = adminRequestEntity.user.isAccountNonLocked ?: true,
            credentialsNonExpired = adminRequestEntity.user.isCredentialsNonExpired ?: true,
            enabled = adminRequestEntity.user.isEnabled ?: true,
            roles = adminRequestEntity.user.roles.map { it.roleName },
        ),
        requestDate = adminRequestEntity.requestDate,
        status = adminRequestEntity.status,
        comment = adminRequestEntity.comment
    )

    fun convertToEntity(user: UserModel): UserEntity = UserEntity(
        username = user.username,
        password = user.password,
        isAccountNonExpired = user.isAccountNonExpired,
        isAccountNonLocked = user.isAccountNonLocked,
        isCredentialsNonExpired = user.isCredentialsNonExpired,
        isEnabled = user.isEnabled,
        roles = user.authorities.mapNotNull { roleRepository.findByRoleName(it.authority) }.toMutableList()
    )
}

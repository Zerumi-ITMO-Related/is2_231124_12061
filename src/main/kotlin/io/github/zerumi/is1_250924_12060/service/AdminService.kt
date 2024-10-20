package io.github.zerumi.is1_250924_12060.service

import io.github.zerumi.is1_250924_12060.dto.AdminRequestDTO
import io.github.zerumi.is1_250924_12060.dto.UserModelDTO
import io.github.zerumi.is1_250924_12060.entity.AdminRequestEntity
import io.github.zerumi.is1_250924_12060.entity.UserEntity
import io.github.zerumi.is1_250924_12060.model.AdminRequestModel
import io.github.zerumi.is1_250924_12060.model.AdminRequestStatus
import io.github.zerumi.is1_250924_12060.model.UserModel
import io.github.zerumi.is1_250924_12060.repository.AdminRequestRepository
import io.github.zerumi.is1_250924_12060.repository.RoleRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class AdminService(
    val adminRequestRepository: AdminRequestRepository,
    val roleRepository: RoleRepository,
    val simpMessagingTemplate: SimpMessagingTemplate
) {
    fun getAllRequests(): List<AdminRequestModel> =
        adminRequestRepository.findAll().mapNotNull { convertToModel(it) }.toList()

    fun newAdminRequest(user: UserModel): AdminRequestModel {
        val request = makeAdminRequest(convertToEntity(user))
        simpMessagingTemplate.convertAndSend("/topic/newAdminRequest", convertToDto(convertToModel(request)))

        adminRequestRepository.save(request)
        return convertToModel(request)
    }

    @Transactional
    fun approveAdminRequest(requestModel: AdminRequestModel, comment: String = "Your request successfully approved"): AdminRequestModel {
        val request = adminRequestRepository.getReferenceById(requestModel.id)

        val adminRole = roleRepository.findByRoleName("ADMIN")!!
        request.user.roles.add(adminRole)

        request.status = AdminRequestStatus.ACCEPTED
        request.comment = comment

        adminRequestRepository.save(request)
        return convertToModel(request)
    }

    @Transactional
    fun declineAdminRequest(requestModel: AdminRequestModel, comment: String = "Your request was rejected"): AdminRequestModel {
        val request = adminRequestRepository.getReferenceById(requestModel.id)

        val adminRole = roleRepository.findByRoleName("ADMIN")!!
        request.user.roles.remove(adminRole)

        request.status = AdminRequestStatus.REJECTED
        request.comment = comment

        adminRequestRepository.save(request)
        return convertToModel(request)
    }

    private fun makeAdminRequest(user: UserEntity) = AdminRequestEntity(
        user = user,
        requestDate = ZonedDateTime.now(),
        status = AdminRequestStatus.PENDING,
        comment = "Request sent"
    )

    private fun convertToDto(adminRequestModel: AdminRequestModel): AdminRequestDTO = AdminRequestDTO(
        id = adminRequestModel.id,
        user = UserModelDTO(
            id = adminRequestModel.user.id,
            username = adminRequestModel.user.username,
            roles = adminRequestModel.user.authorities.mapNotNull { it.authority }
        ),
        requestDate = adminRequestModel.requestDate,
        status = adminRequestModel.status,
        comment = adminRequestModel.comment
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

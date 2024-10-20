package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.AdminRequestDTO
import io.github.zerumi.is1_250924_12060.dto.UserModelDTO
import io.github.zerumi.is1_250924_12060.model.AdminRequestModel
import io.github.zerumi.is1_250924_12060.service.AdminService
import io.github.zerumi.is1_250924_12060.service.UserService
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class AdminController(val adminService: AdminService, val userService: UserService, val simpMessagingTemplate: SimpMessagingTemplate) {
    @GetMapping
    fun getAdminRequests(): List<AdminRequestDTO> = adminService.getAllRequests().map { convertToDto(it) }

    @PutMapping("/approve")
    fun approveRequest(@RequestBody request: AdminRequestDTO) {
        val updated = adminService.approveAdminRequest(convertToModel(request))
        simpMessagingTemplate.convertAndSend("/topic/updatedAdminRequest", updated)
    }

    @PutMapping("/decline")
    fun rejectRequest(@RequestBody request: AdminRequestDTO) {
        val updated = adminService.declineAdminRequest(convertToModel(request))
        simpMessagingTemplate.convertAndSend("/topic/updatedAdminRequest", updated)
    }

    private fun convertToModel(request: AdminRequestDTO): AdminRequestModel = AdminRequestModel(
        id = request.id,
        user = userService.loadUserByUsername(request.user.username),
        requestDate = request.requestDate,
        status = request.status,
        comment = request.comment
    )

    fun convertToDto(adminRequestModel: AdminRequestModel): AdminRequestDTO = AdminRequestDTO(
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
}

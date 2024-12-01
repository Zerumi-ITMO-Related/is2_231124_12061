package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.HumanBeingDTO
import io.github.zerumi.is1_250924_12060.service.MassModelImportService
import io.github.zerumi.is1_250924_12060.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/import")
class ImportController(
    val massModelImportService: MassModelImportService,
    val userService: UserService,
    val simpMessagingTemplate: SimpMessagingTemplate,
) {
    @PostMapping("/upload")
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
        @RequestHeader(HttpHeaders.AUTHORIZATION) auth: String
    ): ResponseEntity<Int> {
        val result = massModelImportService.processInput(file, userService.loadUserBySessionId(auth))
        for (dto in result) {
            simpMessagingTemplate.convertAndSend("/topic/newModel", dto)
        }
        return ResponseEntity.ok(result.size)
    }
}

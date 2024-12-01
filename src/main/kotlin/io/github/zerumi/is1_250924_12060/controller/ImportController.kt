package io.github.zerumi.is1_250924_12060.controller

import io.github.zerumi.is1_250924_12060.dto.HumanBeingDTO
import io.github.zerumi.is1_250924_12060.service.MassModelImportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/import")
class ImportController(
    val massModelImportService: MassModelImportService,
) {
    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<List<HumanBeingDTO>> =
        ResponseEntity.ok(massModelImportService.processInput(file))
}
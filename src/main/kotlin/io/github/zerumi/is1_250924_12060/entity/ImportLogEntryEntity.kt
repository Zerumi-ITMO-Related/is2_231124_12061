package io.github.zerumi.is1_250924_12060.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.ZonedDateTime

@Entity
@Table(name = "import_log_entries", schema = "s367837")
class ImportLogEntryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,
    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "owner_id", unique = false, nullable = false)
    var userEntity: UserEntity,
    var successful: Boolean,
    var importedModels: Int,
    var timestamp: ZonedDateTime,
) : Serializable

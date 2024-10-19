package io.github.zerumi.is1_250924_12060.entity

import io.github.zerumi.is1_250924_12060.model.AdminRequestStatus
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "admin_request", schema = "s367837")
class AdminRequestEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: UserEntity,
    var requestDate: ZonedDateTime,
    @Enumerated(value = EnumType.STRING)
    var status: AdminRequestStatus,
    var comment: String
)

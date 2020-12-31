package com.clnk.livecommerce.api.library.member

import com.clnk.livecommerce.api.library.model.BaseEntity
import java.time.Instant
import javax.persistence.Entity

@Entity
class MemberCert(
    var memberId: Long? = null,
    var certPhoneNumber: String,
    var certNumber: String,
    var statue: CertStatus = CertStatus.REQUEST,
    var requestedAt: Instant,
    var certedAt: Instant? = null,
) : BaseEntity()

enum class CertStatus {
    REQUEST, VERIFIED, EXPIRED
}

package com.clnk.livecommerce.api.member

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class Role(
    @Enumerated(EnumType.STRING)
    @Column(unique = true, length = 30)
    var roleType: RoleType,
    override var id: Long? = null
) : BaseEntity()

enum class RoleType {
    ROLE_GUEST, ROLE_USER, ROLE_MENTO, ROLE_MENTEE
}

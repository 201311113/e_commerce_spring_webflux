package com.clnk.livecommerce.api.library.adminmember

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class RoleAdmin(
    @Enumerated(EnumType.STRING)
    @Column(unique = true, length = 30)
    var roleType: RoleType,
    override var id: Long? = null
) : BaseEntity()

enum class RoleType {
    ROLE_ADMIN
}

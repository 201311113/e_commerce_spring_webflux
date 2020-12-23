package com.clnk.livecommerce.api.library.adminmember

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.*

@Entity
class AdminMember(
    @Column(length = 200)
    var username: String,

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    var status: AdminStatus = AdminStatus.ACTIVE,
    var password: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "adminMember")
    var roles: MutableSet<AdminRole> = mutableSetOf()

) : BaseEntity() {
    internal fun addRole(memberRole: AdminRole) {
        roles.add(memberRole)
        memberRole.adminMember = this
    }
}

enum class AdminStatus {
    ACTIVE, INACTIVE, LEAVE
}

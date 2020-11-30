package com.clnk.livecommerce.api.adminmember

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["admin_member_id", "role_admin_id"])])
class AdminRole(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_member_id")
    var adminMember: AdminMember,

    @ManyToOne
    @JoinColumn(name = "role_admin_id")
    var roleAdmin: RoleAdmin

) : BaseEntity()

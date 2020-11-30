package com.clnk.livecommerce.api.member

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["member_id", "role_id"])])
class MemberRole(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,

    @ManyToOne
    @JoinColumn(name = "role_id")
    var role: Role

) : BaseEntity()

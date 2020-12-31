package com.clnk.livecommerce.api.library.member

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["member_info_id", "role_id"])])
class MemberRole(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_info_id")
    var memberInfo: MemberInfo,

    @ManyToOne
    @JoinColumn(name = "role_id")
    var role: Role

) : BaseEntity()

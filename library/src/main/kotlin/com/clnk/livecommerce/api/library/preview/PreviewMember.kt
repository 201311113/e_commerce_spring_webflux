package com.clnk.livecommerce.api.library.preview

import com.clnk.livecommerce.api.library.member.MemberInfo
import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["member_info_id", "preview_id"])])
class PreviewMember(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_info_id")
    var memberInfo: MemberInfo,

    @ManyToOne
    @JoinColumn(name = "preview_id")
    var preview: Preview

) : BaseEntity()


package com.clnk.livecommerce.api.library.member

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.*

@Entity
class Member(
    @Column(length = 200)
    var snsId: String,
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    var snsType: SnsType,
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.ACTIVE,
    var password: String? = null,
    @Lob
    var snsToken: String? = null,
    @Column(length = 200)
    var nickName: String? = null,
    @Column(length = 100)
    var realName: String? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "member")
    var roles: MutableSet<MemberRole> = mutableSetOf()

) : BaseEntity() {
    internal fun addRole(memberRole: MemberRole) {
        roles.add(memberRole)
        memberRole.member = this
    }
}

enum class MemberStatus {
    ACTIVE, INACTIVE, LEAVE
}

enum class SnsType {
    EMAIL, FACEBOOK, KAKAO, GOOGLE, APPLE,
}

enum class Gender {
    MALE, FEMALE, UNKNOWN
}

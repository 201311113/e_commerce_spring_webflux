package com.clnk.livecommerce.api.library.member

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.*

@Entity
class MemberInfo(
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
    @Column(length = 50)
    var phoneNumber: String? = null,

    var agreeService: Boolean = false,
    var agreeSecurity: Boolean = false,
    var agreeMarketing: Boolean = false,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "memberInfo")
    var roles: MutableSet<MemberRole> = mutableSetOf()

) : BaseEntity() {
    internal fun addRole(memberRole: MemberRole) {
        roles.add(memberRole)
        memberRole.memberInfo = this
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

enum class MemberSearchCondition(val searchKey: String) {
    NICKNAME("nickname"), PHONENUMBER("phonenumber")
}

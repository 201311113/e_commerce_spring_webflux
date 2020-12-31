package com.clnk.livecommerce.api.library.member.repository

import com.clnk.livecommerce.api.library.member.MemberInfo
import com.clnk.livecommerce.api.library.member.SnsType
import org.springframework.data.jpa.repository.JpaRepository

interface MemberInfoRepository : JpaRepository<MemberInfo, Long>, MemberInfoDslRepository {
    fun findBySnsIdAndSnsTypeAndActive(snsId: String, snsType: SnsType, active: Boolean): MemberInfo?
    fun findByIdAndActive(id: Long, active: Boolean): MemberInfo?
    fun findByNickNameAndActive(nickName: String, active: Boolean): MemberInfo?
}
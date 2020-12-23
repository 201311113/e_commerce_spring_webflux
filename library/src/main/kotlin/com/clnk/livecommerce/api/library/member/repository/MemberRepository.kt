package com.clnk.livecommerce.api.library.member.repository

import com.clnk.livecommerce.api.library.member.Member
import com.clnk.livecommerce.api.library.member.SnsType
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findBySnsIdAndSnsTypeAndActive(snsId: String, snsType: SnsType, active: Boolean): Member?
    fun findByIdAndActive(id: Long, active: Boolean): Member?
}
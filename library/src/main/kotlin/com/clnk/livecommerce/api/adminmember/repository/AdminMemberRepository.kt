package com.clnk.livecommerce.api.adminmember.repository

import com.clnk.livecommerce.api.adminmember.AdminMember
import org.springframework.data.jpa.repository.JpaRepository

interface AdminMemberRepository : JpaRepository<AdminMember, Long> {
    fun findByUsernameAndActive(username: String, active: Boolean): AdminMember?
    fun findByIdAndActive(id: Long, active: Boolean): AdminMember?
}
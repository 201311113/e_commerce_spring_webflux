package com.clnk.livecommerce.api.member.repository

import com.clnk.livecommerce.api.member.Role
import com.clnk.livecommerce.api.member.RoleType
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByRoleType(type: RoleType): Role?
}
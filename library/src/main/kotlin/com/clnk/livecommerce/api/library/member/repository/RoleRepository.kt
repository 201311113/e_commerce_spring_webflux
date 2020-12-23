package com.clnk.livecommerce.api.library.member.repository

import com.clnk.livecommerce.api.library.member.Role
import com.clnk.livecommerce.api.library.member.RoleType
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByRoleType(type: RoleType): Role?
}
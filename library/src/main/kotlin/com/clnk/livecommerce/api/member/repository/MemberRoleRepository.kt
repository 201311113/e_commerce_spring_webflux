package com.clnk.livecommerce.api.member.repository

import com.clnk.livecommerce.api.member.MemberRole
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRoleRepository : JpaRepository<MemberRole, Long>
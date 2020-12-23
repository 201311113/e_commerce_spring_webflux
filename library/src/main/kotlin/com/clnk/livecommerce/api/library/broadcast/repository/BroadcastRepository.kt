package com.clnk.livecommerce.api.library.broadcast.repository

import com.clnk.livecommerce.api.library.broadcast.Broadcast
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BroadcastRepository : JpaRepository<Broadcast, Long>, BroadcastDslRepository {
    fun findAllByActive(pageable: Pageable, active: Boolean): Page<Broadcast>
    fun findByIdAndActive(id: Long, active: Boolean): Broadcast?
}
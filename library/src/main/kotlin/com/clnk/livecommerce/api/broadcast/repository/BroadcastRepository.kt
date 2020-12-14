package com.clnk.livecommerce.api.broadcast.repository

import com.clnk.livecommerce.api.broadcast.Broadcast
import com.clnk.livecommerce.api.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BroadcastRepository : JpaRepository<Broadcast, Long>, BroadcastDslRepository {
    fun findAllByActive(pageable: Pageable, active: Boolean): Page<Broadcast>
    fun findByIdAndActive(id: Long, active: Boolean): Broadcast?
}
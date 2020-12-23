package com.clnk.livecommerce.api.library.broadcast.repository

import com.clnk.livecommerce.api.library.broadcast.BroadcastOnSaleItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

interface BroadcastOnSaleItemRepository : JpaRepository<BroadcastOnSaleItem, Long> {
    fun findAllByActive(pageable: Pageable, active: Boolean): Page<BroadcastOnSaleItem>
    fun findByIdAndActive(id: Long, active: Boolean): BroadcastOnSaleItem?

    @Transactional
    @Modifying
    @Query(value = "delete from BroadcastOnSaleItem where onSaleItem.id in (:deletes) and broadcast.id = :broadcastId")
    fun deleteByIds(@Param("deletes") deletes: List<Long>, @Param("broadcastId") broadcastId: Long)
}
package com.clnk.livecommerce.api.onsaleitem.repository

import com.clnk.livecommerce.api.onsaleitem.OnSaleItem
import org.springframework.data.jpa.repository.JpaRepository

interface OnSaleItemRepository : JpaRepository<OnSaleItem, Long> {
    fun findByIdAndActive(id: Long, active: Boolean): OnSaleItem?
}
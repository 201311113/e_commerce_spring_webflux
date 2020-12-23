package com.clnk.livecommerce.api.library.onsaleitem.repository

import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItem
import org.springframework.data.jpa.repository.JpaRepository

interface OnSaleItemRepository : JpaRepository<OnSaleItem, Long>, OnSaleItemDslRepository {
    fun findByIdAndActive(id: Long, active: Boolean): OnSaleItem?
}
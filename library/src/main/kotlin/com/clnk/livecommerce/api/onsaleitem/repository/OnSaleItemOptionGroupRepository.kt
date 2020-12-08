package com.clnk.livecommerce.api.onsaleitem.repository

import com.clnk.livecommerce.api.onsaleitem.OnSaleItemOptionGroup
import org.springframework.data.jpa.repository.JpaRepository

interface OnSaleItemOptionGroupRepository : JpaRepository<OnSaleItemOptionGroup, Long> {
    fun findByIdAndActive(id: Long, active: Boolean): OnSaleItemOptionGroup?
}
package com.clnk.livecommerce.api.library.onsaleitem.repository

import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItemOption
import org.springframework.data.jpa.repository.JpaRepository

interface OnSaleItemOptionRepository : JpaRepository<OnSaleItemOption, Long> {
    fun findByIdAndActive(id: Long, active: Boolean): OnSaleItemOption?
    fun findByIdAndOnSaleItemOptionGroupIdAndActive(id: Long, onSaleItemOptionGroupId: Long, active: Boolean): OnSaleItemOption?
}
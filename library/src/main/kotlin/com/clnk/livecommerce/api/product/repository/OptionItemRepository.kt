package com.clnk.livecommerce.api.product.repository

import com.clnk.livecommerce.api.product.OptionItem
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface OptionItemRepository : JpaRepository<OptionItem, Long> {
    fun findAllByOptionGroupIdAndActive(pageable: Pageable, optionGroupId: Long, active: Boolean): List<OptionItem>
    fun findByIdAndActive(id: Long, active: Boolean): OptionItem?
}
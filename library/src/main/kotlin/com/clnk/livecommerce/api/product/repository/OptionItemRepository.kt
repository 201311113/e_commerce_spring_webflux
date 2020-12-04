package com.clnk.livecommerce.api.product.repository

import com.clnk.livecommerce.api.product.OptionGroup
import com.clnk.livecommerce.api.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface OptionGroupRepository : JpaRepository<OptionGroup, Long> {
    fun findAllByProductIdAndActive(pageable: Pageable, productId: Long, active: Boolean): List<OptionGroup>
    fun findByIdAndActive(id: Long, active: Boolean): OptionGroup?
}
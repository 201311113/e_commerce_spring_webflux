package com.clnk.livecommerce.api.library.product.repository

import com.clnk.livecommerce.api.library.product.OptionGroup
import org.springframework.data.jpa.repository.JpaRepository

interface OptionGroupRepository : JpaRepository<OptionGroup, Long> {
    fun findAllByProductIdAndActive(productId: Long, active: Boolean): List<OptionGroup>
    fun findAllByProductIdAndActiveOrderBySortPositionAscIdDesc(productId: Long, active: Boolean): List<OptionGroup>
    fun findByIdAndActive(id: Long, active: Boolean): OptionGroup?
    fun findByIdAndProductIdAndActive(id: Long, productId: Long, active: Boolean): OptionGroup?

}
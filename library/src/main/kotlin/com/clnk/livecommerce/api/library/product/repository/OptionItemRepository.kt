package com.clnk.livecommerce.api.library.product.repository

import com.clnk.livecommerce.api.library.product.OptionItem
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

interface OptionItemRepository : JpaRepository<OptionItem, Long> {
    fun findAllByOptionGroupIdAndActive(pageable: Pageable, optionGroupId: Long, active: Boolean): List<OptionItem>
    fun findByIdAndActive(id: Long, active: Boolean): OptionItem?
    fun findByIdAndOptionGroupIdAndActive(id: Long, optionGroupId: Long, active: Boolean): OptionItem?

    @Transactional
    @Modifying
    @Query(value = "update OptionItem set active = false where id in (:deletes) and optionGroup.id = :optionGroupId")
    fun deleteByIds(@Param("deletes") deletes: List<Long>, @Param("optionGroupId") optionGroupId: Long)
}
package com.cucurbita.api.mentoitem.repository

import com.cucurbita.api.mentoitem.ItemStatus
import com.cucurbita.api.mentoitem.MentoItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MentoItemRepository : JpaRepository<MentoItem, Long> {
    fun findAllByActive(pageable: Pageable, active: Boolean): Page<MentoItem>
    fun findAllByActiveAndStatus(pageable: Pageable, active: Boolean, status: ItemStatus): Page<MentoItem>
    fun findByIdAndActive(id: Long, active: Boolean): MentoItem?
    fun findAllByMentoIdAndActive(pageable: Pageable, mentoId: Long, active: Boolean): Page<MentoItem>
    fun findByIdAndStatusAndActive(id: Long, itemStatus: ItemStatus, active: Boolean): MentoItem?
}
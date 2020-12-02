package com.cucurbita.api.mentoitem.service

import com.cucurbita.api.mentoitem.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MentoItemService {
    fun create(req: CreateMentoItemReq, memberId: Long): CreateMentoItemRes
    fun findAllByMemberId(pageable: Pageable, memberId: Long): Page<MentoItemResForList>
    fun findAllByStatus(pageable: Pageable): Page<MentoItemResForList>
    fun findById(id: Long): MentoItemRes
    fun updateStatus(adminId: Long, id: Long): MentoItemStatusRes
    fun findAll(pageable: Pageable): Page<MentoItemResForList>
}
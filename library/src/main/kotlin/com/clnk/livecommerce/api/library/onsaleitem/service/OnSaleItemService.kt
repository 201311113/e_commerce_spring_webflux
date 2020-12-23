package com.clnk.livecommerce.api.onsaleitem.service

import com.clnk.livecommerce.api.library.onsaleitem.CreateOnSaleItemReq
import com.clnk.livecommerce.api.library.onsaleitem.CreateOnSaleItemRes
import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItemRes
import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItemResForList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface OnSaleItemService {
    fun create(req: CreateOnSaleItemReq, adminId: Long): CreateOnSaleItemRes
    fun findById(id: Long, adminId: Long): OnSaleItemRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<OnSaleItemResForList>
    fun update(id: Long, req: CreateOnSaleItemReq, adminId: Long): CreateOnSaleItemRes
}
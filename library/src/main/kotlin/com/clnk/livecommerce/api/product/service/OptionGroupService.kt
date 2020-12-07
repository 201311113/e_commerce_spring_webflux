package com.clnk.livecommerce.api.product.service

import com.clnk.livecommerce.api.product.*

interface OptionGroupService {
    fun create(productId: Long, req: CreateOptionReq, adminId: Long): CreateOptionRes
    fun findAllByProductId(productId: Long): OptionGroupListRes
    fun findById(id: Long): OptionGroupRes
    fun update(id: Long, req: UpdateOptionReq, adminId: Long): CreateOptionRes
    fun updateOptionGroupSort(req: UpdateOptionGroupSortReq, adminId: Long): CreateOptionRes
    
}
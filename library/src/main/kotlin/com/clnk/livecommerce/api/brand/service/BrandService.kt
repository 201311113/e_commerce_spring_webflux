package com.clnk.livecommerce.api.brand.service

import com.clnk.livecommerce.api.brand.BrandRes
import com.clnk.livecommerce.api.brand.CreateBrandReq
import com.clnk.livecommerce.api.brand.CreateBrandRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface BrandService {
    fun create(req: CreateBrandReq, adminId: Long): CreateBrandRes
    fun findById(id: Long, adminId: Long): BrandRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<BrandRes>
    fun update(id: Long, req: CreateBrandReq, adminId: Long): CreateBrandRes
}
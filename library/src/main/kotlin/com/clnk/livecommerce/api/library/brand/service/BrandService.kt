package com.clnk.livecommerce.api.library.brand.service

import com.clnk.livecommerce.api.library.brand.BrandRes
import com.clnk.livecommerce.api.library.brand.CreateBrandReq
import com.clnk.livecommerce.api.library.brand.CreateBrandRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface BrandService {
    fun create(req: CreateBrandReq, adminId: Long): CreateBrandRes
    fun findById(id: Long, adminId: Long): BrandRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<BrandRes>
    fun update(id: Long, req: CreateBrandReq, adminId: Long): CreateBrandRes
}
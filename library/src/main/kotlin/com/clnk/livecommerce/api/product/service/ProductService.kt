package com.clnk.livecommerce.api.product.service

import com.clnk.livecommerce.api.product.CreateProductReq
import com.clnk.livecommerce.api.product.CreateProductRes
import com.clnk.livecommerce.api.product.ProductListRes
import com.clnk.livecommerce.api.product.ProductRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface ProductService {
    fun create(req: CreateProductReq, adminId: Long): CreateProductRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<ProductListRes>
    fun findById(id: Long): ProductRes
    fun update(id: Long, req: CreateProductReq, adminId: Long): CreateProductRes
}
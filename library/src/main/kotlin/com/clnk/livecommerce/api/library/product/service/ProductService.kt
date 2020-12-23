package com.clnk.livecommerce.api.library.product.service

import com.clnk.livecommerce.api.library.product.CreateProductReq
import com.clnk.livecommerce.api.library.product.CreateProductRes
import com.clnk.livecommerce.api.library.product.ProductListRes
import com.clnk.livecommerce.api.library.product.ProductRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface ProductService {
    fun create(req: CreateProductReq, adminId: Long): CreateProductRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<ProductListRes>
    fun findById(id: Long): ProductRes
    fun update(id: Long, req: CreateProductReq, adminId: Long): CreateProductRes
}
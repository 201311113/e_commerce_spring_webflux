package com.clnk.livecommerce.api.library.product.repository

import com.clnk.livecommerce.api.library.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface ProductDslRepository {
    fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Product>
}


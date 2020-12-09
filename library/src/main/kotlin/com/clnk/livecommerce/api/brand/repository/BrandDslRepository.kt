package com.clnk.livecommerce.api.brand.repository

import com.clnk.livecommerce.api.brand.Brand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface BrandDslRepository {
    fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Brand>
}


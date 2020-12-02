package com.clnk.livecommerce.api.product.repository

import com.clnk.livecommerce.api.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByActive(pageable: Pageable, active: Boolean): Page<Product>
}
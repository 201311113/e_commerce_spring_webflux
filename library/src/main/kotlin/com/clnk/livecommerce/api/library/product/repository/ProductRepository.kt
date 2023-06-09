package com.clnk.livecommerce.api.library.product.repository

import com.clnk.livecommerce.api.library.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>, ProductDslRepository {
    fun findAllByActive(pageable: Pageable, active: Boolean): Page<Product>
    fun findByIdAndActive(id: Long, active: Boolean): Product?
}
package com.clnk.livecommerce.api.library.brand.repository

import com.clnk.livecommerce.api.library.brand.Brand
import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<Brand, Long>, BrandDslRepository {
    fun findByIdAndActive(id: Long, active: Boolean): Brand?
}
package com.clnk.livecommerce.api.library.preview.repository

import com.clnk.livecommerce.api.library.preview.Preview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PreviewRepository : JpaRepository<Preview, Long>, PreviewDslRepository {
    fun findAllByActive(pageable: Pageable, active: Boolean): Page<Preview>
    fun findByIdAndActive(id: Long, active: Boolean): Preview?
}
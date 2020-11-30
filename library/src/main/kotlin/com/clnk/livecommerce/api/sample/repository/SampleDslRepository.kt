package com.clnk.livecommerce.api.sample.repository

import com.clnk.livecommerce.api.sample.Sample
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SampleDslRepository {
    fun findAllByPage(pageable: Pageable): Page<Sample>
}
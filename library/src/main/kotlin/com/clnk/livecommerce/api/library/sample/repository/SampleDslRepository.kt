package com.clnk.livecommerce.api.library.sample.repository

import com.clnk.livecommerce.api.library.sample.Sample
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SampleDslRepository {
    fun findAllByPage(pageable: Pageable): Page<Sample>
}
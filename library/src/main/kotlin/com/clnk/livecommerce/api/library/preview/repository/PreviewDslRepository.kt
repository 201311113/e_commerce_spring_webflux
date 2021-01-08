package com.clnk.livecommerce.api.library.preview.repository

import com.clnk.livecommerce.api.library.preview.Preview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface PreviewDslRepository {
    fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Preview>
}


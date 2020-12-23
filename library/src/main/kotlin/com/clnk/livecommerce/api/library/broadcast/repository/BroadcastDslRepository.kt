package com.clnk.livecommerce.api.library.broadcast.repository

import com.clnk.livecommerce.api.library.broadcast.Broadcast
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface BroadcastDslRepository {
    fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Broadcast>
}


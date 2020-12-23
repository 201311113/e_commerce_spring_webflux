package com.clnk.livecommerce.api.library.sample.service

import com.clnk.livecommerce.api.library.sample.CreateSampleReq
import com.clnk.livecommerce.api.library.sample.CreateSampleRes
import com.clnk.livecommerce.api.library.sample.SampleRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SampleService {
    fun allSamples(pageable: Pageable): Page<SampleRes>
    fun findById(id: Long): SampleRes
    fun create(req: CreateSampleReq): CreateSampleRes
}
package com.clnk.livecommerce.api.library.preview.service

import com.clnk.livecommerce.api.library.preview.CreatePreviewReq
import com.clnk.livecommerce.api.library.preview.CreatePreviewRes
import com.clnk.livecommerce.api.library.preview.PreviewRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface PreviewService {
    fun create(req: CreatePreviewReq, adminId: Long): CreatePreviewRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<PreviewRes>
    fun findById(id: Long): PreviewRes
    fun update(id: Long, req: CreatePreviewReq, adminId: Long): CreatePreviewRes
}
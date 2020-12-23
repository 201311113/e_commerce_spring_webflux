package com.clnk.livecommerce.api.library.broadcast.service

import com.clnk.livecommerce.api.library.broadcast.BroadcastReq
import com.clnk.livecommerce.api.library.broadcast.BroadcastRes
import com.clnk.livecommerce.api.library.broadcast.CreateBroadcastRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface BroadcastService {
    fun create(req: BroadcastReq, adminId: Long): CreateBroadcastRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<BroadcastRes>
    fun findById(id: Long): BroadcastRes
    fun update(id: Long, req: BroadcastReq, adminId: Long): CreateBroadcastRes
}
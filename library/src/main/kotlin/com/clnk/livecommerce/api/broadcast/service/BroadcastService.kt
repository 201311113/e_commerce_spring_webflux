package com.clnk.livecommerce.api.broadcast.service

import com.clnk.livecommerce.api.broadcast.BroadcastReq
import com.clnk.livecommerce.api.broadcast.BroadcastRes
import com.clnk.livecommerce.api.broadcast.CreateBroadcastRes
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface BroadcastService {
    fun create(req: BroadcastReq, adminId: Long): CreateBroadcastRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<BroadcastRes>
    fun findById(id: Long): BroadcastRes
    fun update(id: Long, req: BroadcastReq, adminId: Long): CreateBroadcastRes
}
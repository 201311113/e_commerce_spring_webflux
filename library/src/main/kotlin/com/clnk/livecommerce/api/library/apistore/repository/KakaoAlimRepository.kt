package com.clnk.livecommerce.api.library.apistore.repository

import com.clnk.livecommerce.api.library.apistore.KakaoAlim
import org.springframework.data.jpa.repository.JpaRepository

interface KakaoAlimRepository : JpaRepository<KakaoAlim, Long> {
    fun findByIdAndActive(id: Long, active: Boolean): KakaoAlim?
}
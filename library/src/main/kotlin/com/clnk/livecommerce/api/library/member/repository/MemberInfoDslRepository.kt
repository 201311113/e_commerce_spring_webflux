package com.clnk.livecommerce.api.library.member.repository

import com.clnk.livecommerce.api.library.member.MemberInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface MemberInfoDslRepository {
    fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<MemberInfo>
}


package com.clnk.livecommerce.api.library.onsaleitem.repository

import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface OnSaleItemDslRepository {
    fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<OnSaleItem>
}


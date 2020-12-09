package com.clnk.livecommerce.api.onsaleitem.repository

import com.clnk.livecommerce.api.onsaleitem.OnSaleItem
import com.clnk.livecommerce.api.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface OnSaleItemDslRepository {
    fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<OnSaleItem>
}


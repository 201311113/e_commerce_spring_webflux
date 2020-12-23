package com.clnk.livecommerce.api.library.product.service

import com.clnk.livecommerce.api.library.product.OptionItemRes

interface OptionService {
    fun findById(id: Long): OptionItemRes
}
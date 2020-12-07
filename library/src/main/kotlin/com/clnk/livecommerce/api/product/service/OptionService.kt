package com.clnk.livecommerce.api.product.service

import com.clnk.livecommerce.api.product.OptionGroupRes
import com.clnk.livecommerce.api.product.OptionItemRes

interface OptionService {
    fun findById(id: Long): OptionItemRes
}
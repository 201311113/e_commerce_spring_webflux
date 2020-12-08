package com.clnk.livecommerce.api.onsaleitem.service

import com.clnk.livecommerce.api.onsaleitem.CreateOnSaleItemReq
import com.clnk.livecommerce.api.onsaleitem.CreateOnSaleItemRes

interface OnSaleItemService {
    fun create(req: CreateOnSaleItemReq, adminId: Long): CreateOnSaleItemRes

}
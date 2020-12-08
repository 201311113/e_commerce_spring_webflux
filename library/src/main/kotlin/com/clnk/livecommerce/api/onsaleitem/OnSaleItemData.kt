package com.clnk.livecommerce.api.onsaleitem

import java.math.BigDecimal

data class CreateOnSaleItemReq(
    var productId: Long,
    var title: String,
    var sellPrice: BigDecimal,
    var stock: Int,
    var description: String,
    var hashTags: String? = null,
    var optionGroups: MutableList<OnSaleItemOptionGroupReq> = mutableListOf()
)

data class OnSaleItemOptionGroupReq(
    var optionGroupId: Long,
    var onSaleItemOptions: MutableList<OnSaleItemOptionReq> = mutableListOf()
)

data class OnSaleItemOptionReq(
    var optionPrice: BigDecimal = BigDecimal.ZERO,
    var stock: Int,
    var optionItemId: Long
)

data class CreateOnSaleItemRes(
    var id: Long = -1
)
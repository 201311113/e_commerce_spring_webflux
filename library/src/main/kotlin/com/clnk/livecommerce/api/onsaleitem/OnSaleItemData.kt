package com.clnk.livecommerce.api.onsaleitem

import com.clnk.livecommerce.api.media.MediaRes
import com.clnk.livecommerce.api.product.OptionItemRes
import java.math.BigDecimal
import java.time.Instant

data class CreateOnSaleItemReq(
    var productId: Long,
    var title: String,
    var sellPrice: BigDecimal,
    var stock: Int,
    var description: String,
    var hashTags: String? = null,
    var deliveryPrice: BigDecimal,
    var isGroupdelivery: Boolean,
    var onSaleItemOptionGroups: MutableList<OnSaleItemOptionGroupReq> = mutableListOf()
)

data class OnSaleItemOptionGroupReq(
    var id: Long = -1,
    var optionGroupId: Long,
    var onSaleItemOptions: MutableList<OnSaleItemOptionReq> = mutableListOf()
)

data class OnSaleItemOptionReq(
    var id: Long = -1,
    var optionPrice: BigDecimal = BigDecimal.ZERO,
    var stock: Int,
    var optionItemId: Long
)

data class CreateOnSaleItemRes(
    var id: Long = -1
)


data class OnSaleItemRes(
    var id: Long = -1,
    var title: String? = null,
    var sellPrice: BigDecimal? = null,
    var stock: Int? = null,
    var description: String? = null,
    var hashTags: String? = null,
    var product: ProductResForOnSaleItem? = null,
    var deliveryPrice: BigDecimal? = null,
    var isGroupdelivery: Boolean? = null,
    var onSaleItemOptionGroups: MutableList<OnSaleItemOptionGroupRes> = mutableListOf()
)

data class OnSaleItemResForList(
    var id: Long = -1,
    var title: String? = null,
    var sellPrice: BigDecimal? = null,
    var stock: Int? = null,
    var description: String? = null,
    var hashTags: String? = null,
    var product: ProductResForOnSaleItem? = null,
    var deliveryPrice: BigDecimal? = null,
    var isGroupdelivery: Boolean? = null
)

data class ProductResForOnSaleItem(
    var id: Long = -1,
    var name: String? = null,
    var description: String? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var createdAt: Instant? = null
)

data class OnSaleItemOptionGroupRes(
    var id: Long = -1,
    var optionGroup: OptionGroupResForOnSaleItem? = null,
    var onSaleItemOptions: MutableList<OnSaleItemOptionRes> = mutableListOf()
)

data class OptionGroupResForOnSaleItem(
    var id: Long = -1,
    var title: String? = null,
    var isRequired: Boolean? = null,
    var sortPosition: Int? = null
)

data class OnSaleItemOptionRes(
    var id: Long = -1,
    var optionPrice: BigDecimal = BigDecimal.ZERO,
    var stock: Int? = null,
    var optionItem: OptionItemRes? = null
)



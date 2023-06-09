package com.clnk.livecommerce.api.library.product

import com.clnk.livecommerce.api.library.brand.BrandRes
import com.clnk.livecommerce.api.library.media.MediaReq
import com.clnk.livecommerce.api.library.media.MediaRes
import com.clnk.livecommerce.api.library.media.NewImage
import java.time.Instant
import javax.validation.constraints.NotBlank

data class CreateProductReq(
    @get:NotBlank
    var name: String,
    var description: String,
    var brandId: Long,
    var updatedImages: MutableList<MediaReq> = mutableListOf(),
    var newImages: MutableList<NewImage> = mutableListOf(),
    var deletedImages: MutableList<Long> = mutableListOf()
)
//
//data class ProductMediaReq(
//    var productImage: FilePart? = null,
//    var sortPosition: Int = 0
//)

data class CreateProductRes(var id: Long = -1)

data class ProductListRes(
    var id: Long = -1,
    var name: String? = null,
    var description: String? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var createdAt: Instant? = null
)

data class ProductRes(
    var id: Long = -1,
    var name: String? = null,
    var description: String? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var optionGroups: MutableList<OptionGroupRes> = mutableListOf(),
    var createdAt: Instant? = null,
    var brand: BrandRes? = null,
)

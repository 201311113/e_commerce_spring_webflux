package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.media.MediaRes
import org.springframework.http.codec.multipart.FilePart
import java.time.Instant
import javax.validation.constraints.NotBlank

data class CreateProductReq(
    @get:NotBlank
    var name: String,
    var description: String,
    var medias: MutableList<ProductMediaReq> = mutableListOf()
)

data class ProductMediaReq(
    var itemImage: FilePart? = null,
    var sortPosition: Int = 0
)

data class CreateProductRes(var id: Long = -1)

data class ProductRes(
    var id: Long = -1,
    var name: String? = null,
    var description: String? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var createdAt: Instant? = null
)

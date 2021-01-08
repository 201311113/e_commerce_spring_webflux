package com.clnk.livecommerce.api.library.preview

import com.clnk.livecommerce.api.library.media.MediaReq
import com.clnk.livecommerce.api.library.media.MediaRes
import com.clnk.livecommerce.api.library.media.NewImage
import java.time.Instant
import javax.validation.constraints.NotBlank

data class CreatePreviewReq(
    @get:NotBlank
    var title: String,
    var description: String,
    var startAt: Instant,
    var endAt: Instant,
    var sortPosition: Int,
    var updatedImages: MutableList<MediaReq> = mutableListOf(),
    var newImages: MutableList<NewImage> = mutableListOf(),
    var deletedImages: MutableList<Long> = mutableListOf()
)

data class CreatePreviewRes(var id: Long = -1)

data class PreviewRes(
    var id: Long = -1,
    var title: String? = null,
    var description: String? = null,
    var startAt: Instant? = null,
    var endAt: Instant? = null,
    var sortPosition: Int? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var createdAt: Instant? = null
)

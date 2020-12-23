package com.clnk.livecommerce.api.library.broadcast

import com.clnk.livecommerce.api.library.media.MediaReq
import com.clnk.livecommerce.api.library.media.MediaRes
import com.clnk.livecommerce.api.library.media.NewImage
import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItemRes
import java.time.Instant

data class BroadcastReq(
    var title: String,
    var description: String,
    var startAt: Instant,
    var endAt: Instant,
    var onSaleItemIds: MutableList<Long> = mutableListOf(),
    var deletedOnSaleItemIds: MutableList<Long> = mutableListOf(),
    var updatedImages: MutableList<MediaReq> = mutableListOf(),
    var newImages: MutableList<NewImage> = mutableListOf(),
    var deletedImages: MutableList<Long> = mutableListOf()
)

data class CreateBroadcastRes(
    var id: Long = -1
)

data class BroadcastRes(
    var id: Long = -1,
    var channelUuid: String? = null,
    var title: String? = null,
    var description: String? = null,
    var startAt: Instant? = null,
    var endAt: Instant? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var onSaleItems: MutableList<BroadcastOnSaleItemRes> = mutableListOf(),
    var createdAt: Instant? = null
)

data class BroadcastOnSaleItemRes(
    var id: Long = -1,
    var onSaleItem: OnSaleItemRes? = null
)

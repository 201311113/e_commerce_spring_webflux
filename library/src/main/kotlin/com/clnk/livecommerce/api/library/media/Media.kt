package com.clnk.livecommerce.api.library.media

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Media(
    @Column(name = "media_uuid", length = 24)
    var mediaUuid: String,
    var url: String,
    var isDefault: Boolean = false,
    var mediaType: MediaType = MediaType.IMAGE,
    var originName: String? = "",
    var modifyName: String? = "",
    var sortPosition: Int = 0,
    var pathS3: String? = "",
    @Column(length = 10)
    var imageExt: String? = "",
    var mediaKey: String? = ""
) : BaseEntity()

enum class MediaType {
    IMAGE, YOUTUBE, VIMEO
}

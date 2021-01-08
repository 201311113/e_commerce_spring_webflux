package com.clnk.livecommerce.api.library.preview

import com.clnk.livecommerce.api.library.media.Media
import com.clnk.livecommerce.api.library.model.BaseEntity
import org.apache.commons.lang3.RandomStringUtils
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class Preview(
    @Column(length = 250)
    var title: String,
    @Column(name = "media_uuid", length = 24)
    var mediaUuid: String = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(Instant.now()) + RandomStringUtils.randomAlphabetic(16),
    @Lob
    var description: String,

    var startAt: Instant,
    var endAt: Instant,
    var sortPosition: Int,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_uuid", referencedColumnName = "media_uuid", insertable = false, updatable = false)
    @OrderBy(value = "sort_position ASC")
    var medias: MutableList<Media> = mutableListOf()

) : BaseEntity()


enum class PreviewSearchCondition(val searchKey: String) {
    TITLE("title"), DESCRIPTION("description")
}
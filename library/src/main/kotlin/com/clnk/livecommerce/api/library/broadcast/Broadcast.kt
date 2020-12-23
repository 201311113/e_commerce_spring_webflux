package com.clnk.livecommerce.api.library.broadcast

import com.clnk.livecommerce.api.library.media.Media
import com.clnk.livecommerce.api.library.model.BaseEntity
import org.apache.commons.lang3.RandomStringUtils
import org.hibernate.annotations.Where
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class Broadcast(
    @Column(length = 250)
    var title: String,
    @Column(name = "media_uuid", length = 24)
    var mediaUuid: String = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(Instant.now()) + RandomStringUtils.randomAlphabetic(16),
    @Column(length = 12)
    var channelUuid: String = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(Instant.now()) + RandomStringUtils.randomAlphabetic(4),
    @Lob
    var description: String,

    var startAt: Instant,
    var endAt: Instant,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_uuid", referencedColumnName = "media_uuid", insertable = false, updatable = false)
    @OrderBy(value = "sort_position ASC")
    var medias: MutableList<Media> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "broadcast_id", insertable = false, updatable = false)
    @OrderBy(value = "id DESC")
    @Where(clause = "active = true")
    var onSaleItems: MutableList<BroadcastOnSaleItem> = mutableListOf()

) : BaseEntity()


enum class BroadcastSearchCondition(val searchKey: String) {
    TITLE("title"), DESCRIPTION("description")
}
package com.cucurbita.api.mentoitem

import com.cucurbita.api.category.MentoItemCategory
import com.cucurbita.api.media.Media
import com.cucurbita.api.mento.Mento
import com.cucurbita.api.model.BaseEntity
import org.apache.commons.lang3.RandomStringUtils
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
data class MentoItem(
    var title: String,
    @Enumerated(EnumType.STRING)
    var status: ItemStatus = ItemStatus.INIT,
    var sellPrice: BigDecimal,
    var stock: Int,
    @Lob
    var description: String,
    var startedAt: Instant?,
    var endedAt: Instant?,
    var hashTags: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mento_id")
    var mento: Mento,

    @Column(name = "media_uuid", length = 32)
    var mediaUuid: String = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(Instant.now()) + RandomStringUtils.randomAlphabetic(16),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "media_uuid", referencedColumnName = "media_uuid", insertable = false, updatable = false)
    @OrderBy(value = "sort_position ASC")
    var medias: MutableList<Media> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "mento_item_id", insertable = false, updatable = false)
    var categorys: MutableList<MentoItemCategory> = mutableListOf(),
) : BaseEntity()

enum class ItemStatus {
    INIT, LISTED, EXPIRED, REJECTED
}

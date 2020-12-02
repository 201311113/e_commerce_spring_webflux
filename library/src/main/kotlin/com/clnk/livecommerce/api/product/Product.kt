package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.media.Media
import com.clnk.livecommerce.api.model.BaseEntity
import org.apache.commons.lang3.RandomStringUtils
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
class Product(
    @Column(length = 200)
    var name: String,
    @Column(name = "media_uuid", length = 32)
    var mediaUuid: String = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(Instant.now()) + RandomStringUtils.randomAlphabetic(16),
    @Lob
    var description: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "media_uuid", referencedColumnName = "media_uuid", insertable = false, updatable = false)
    @OrderBy(value = "sort_position ASC")
    var medias: MutableList<Media> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "product_id", nullable = true, insertable = false, updatable = false)
    var optionGroups: MutableList<OptionGroup> = mutableListOf()

) : BaseEntity()

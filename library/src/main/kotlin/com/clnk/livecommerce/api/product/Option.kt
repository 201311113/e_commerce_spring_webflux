package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.media.Media
import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.*

@Entity
class OptionGroup(
    @Column(length = 200)
    var title: String,
    var isRequired: Boolean,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "media_uuid", referencedColumnName = "media_uuid", insertable = false, updatable = false)
    @OrderBy(value = "sort_position ASC")
    var medias: MutableList<Media> = mutableListOf()

) : BaseEntity()

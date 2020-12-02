package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.*

@Entity
class OptionGroup(
    @Column(length = 200)
    var title: String,
    var isRequired: Boolean,
    var sortPosition: Int = 0,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "optionGroup")
    @OrderBy(value = "sort_position ASC")
    var options: MutableList<OptionItem> = mutableListOf()

) : BaseEntity()

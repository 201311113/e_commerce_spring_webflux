package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.*

@Entity
class OptionGroup(
    @Column(length = 200)
    var title: String,
    var isRequired: Boolean,
    var sortPosition: Int = 0,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "option_group_id", insertable = false, updatable = false)
    @OrderBy(value = "sort_position ASC")
    var options: MutableList<Option> = mutableListOf()

) : BaseEntity()

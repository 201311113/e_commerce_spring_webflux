package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.*

@Entity
class OptionGroup(
    @Column(length = 200)
    var title: String,
    var isRequired: Boolean,
    var sortPosition: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "option_group_id", referencedColumnName = "id", nullable = true)
    @OrderBy(value = "sort_position ASC")
    var optionItems: MutableList<OptionItem> = mutableListOf()

) : BaseEntity()

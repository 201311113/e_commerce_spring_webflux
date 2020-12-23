package com.clnk.livecommerce.api.library.product

import com.clnk.livecommerce.api.library.model.BaseEntity
import org.hibernate.annotations.Where
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
    @Where(clause="active = true")
    var optionItems: MutableList<OptionItem> = mutableListOf()

) : BaseEntity()

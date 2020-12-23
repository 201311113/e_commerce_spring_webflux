package com.clnk.livecommerce.api.library.product

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.*

@Entity
class OptionItem(

    @Column(length = 200)
    var name: String,
    var sortPosition: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "option_group_id")
    var optionGroup: OptionGroup
) : BaseEntity()

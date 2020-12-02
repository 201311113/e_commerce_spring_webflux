package com.clnk.livecommerce.api.product

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Option(
    var optionGroupId: Long,
    @Column(length = 200)
    var name: String,
    var sortPosition: Int = 0,
) : BaseEntity()

package com.clnk.livecommerce.api.onsaleitem

import com.clnk.livecommerce.api.model.BaseEntity
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OnSaleItemOption(
    var optionPrice: BigDecimal = BigDecimal.ZERO,
    var sortPosition: Int = 0,
    var stock: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    var onSaleItemOptionGroup: OnSaleItemOptionGroup
) : BaseEntity()

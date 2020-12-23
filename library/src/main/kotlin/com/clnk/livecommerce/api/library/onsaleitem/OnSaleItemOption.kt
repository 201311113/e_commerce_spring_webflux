package com.clnk.livecommerce.api.library.onsaleitem

import com.clnk.livecommerce.api.library.model.BaseEntity
import com.clnk.livecommerce.api.library.product.OptionItem
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Entity
class OnSaleItemOption(
    var optionPrice: BigDecimal = BigDecimal.ZERO,
    var stock: Int,

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "on_sale_item_option_group_id")
    var onSaleItemOptionGroup: OnSaleItemOptionGroup,

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "option_item_id")
    var optionItem: OptionItem

) : BaseEntity()

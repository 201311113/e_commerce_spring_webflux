package com.clnk.livecommerce.api.onsaleitem

import com.clnk.livecommerce.api.model.BaseEntity
import com.clnk.livecommerce.api.product.OptionGroup
import javax.persistence.*

@Entity
class OnSaleItemOptionGroup(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "on_sale_item_id")
    var onSaleItem: OnSaleItem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    var optionGroup: OptionGroup,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "option_group_id", referencedColumnName = "id", nullable = true)
    var onSaleItemOptions: MutableList<OnSaleItemOption> = mutableListOf()

) : BaseEntity()

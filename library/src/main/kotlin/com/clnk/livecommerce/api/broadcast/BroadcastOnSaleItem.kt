package com.clnk.livecommerce.api.broadcast

import com.clnk.livecommerce.api.model.BaseEntity
import com.clnk.livecommerce.api.onsaleitem.OnSaleItem
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["broadcast_id", "on_sale_item_id"])])
class BroadcastOnSaleItem(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broadcast_id")
    var broadcast: Broadcast,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "on_sale_item_id")
    var onSaleItem: OnSaleItem

) : BaseEntity()


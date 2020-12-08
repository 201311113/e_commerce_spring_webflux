package com.clnk.livecommerce.api.onsaleitem

import com.clnk.livecommerce.api.model.BaseEntity
import com.clnk.livecommerce.api.product.Product
import java.math.BigDecimal
import java.time.Instant
import javax.persistence.*

@Entity
data class OnSaleItem(
    var title: String,
    @Enumerated(EnumType.STRING)
    var status: ItemStatus = ItemStatus.INIT,
    var sellPrice: BigDecimal,
    var stock: Int,
    @Lob
    var description: String? = null,
    var startedAt: Instant?= null,
    var endedAt: Instant?= null,
    var hashTags: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product

) : BaseEntity()

enum class ItemStatus {
    INIT, LISTED, EXPIRED, REJECTED
}

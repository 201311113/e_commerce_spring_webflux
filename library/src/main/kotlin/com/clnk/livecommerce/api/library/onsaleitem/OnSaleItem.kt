package com.clnk.livecommerce.api.library.onsaleitem

import com.clnk.livecommerce.api.library.model.BaseEntity
import com.clnk.livecommerce.api.library.product.Product
import org.hibernate.annotations.Where
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
    var deliveryPrice: BigDecimal,
    var isGroupdelivery: Boolean, //묶음배송 여부

    @Lob
    var description: String? = null,
    var startedAt: Instant? = null,
    var endedAt: Instant? = null,
    var hashTags: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "on_sale_item_id", nullable = true, insertable = false, updatable = false)
    @OrderBy(value = "id")
    @Where(clause = "active = true")
    var onSaleItemOptionGroups: MutableList<OnSaleItemOptionGroup> = mutableListOf()

) : BaseEntity()

enum class ItemStatus {
    INIT, LISTED, EXPIRED, REJECTED
}

enum class OnSaleItemSearchCondition(val searchKey: String) {
    TITLE("title"), DESCRIPTION("description"), HASHTAGS("hashTags"), PRODUCTNAME("productname")
}

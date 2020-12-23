package com.clnk.livecommerce.api.library.member

import com.clnk.livecommerce.api.library.model.BaseEntity
import org.hibernate.annotations.Type
import java.time.Instant
import javax.persistence.*

@Entity
class Address(

    @Column(length = 10)
    var postCode: String,

    @Column(length = 100)
    var address1: String,

    @Column(length = 100)
    var address2: String,

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    var addressType: AddressType,

    @Column(length = 11)
    var phoneNumber: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var member: Member,

    @Type(type = "yes_no")
    @Column(columnDefinition = "CHAR", length = 1, nullable = false)
    var isDefault: Boolean = false,

    @Column(length = 100)
    var title: String? = null,

    @Column(length = 100)
    var name: String? = null,

    var canceledAt: Instant? = null

) : BaseEntity()

enum class AddressType {
    JIBUN, ROUTE
}
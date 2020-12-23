package com.clnk.livecommerce.api.library.brand

import com.clnk.livecommerce.api.library.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Brand(
    @Column(length = 100, nullable = false)
    var name: String,
    var managerName: String,
    var managerPhoneNumber: String,
    id: Long? = null
) : BaseEntity()


enum class BrandSearchCondition(val searchKey: String) {
    NAME("name"), MANAGERNAME("managerName"), MANAGERPHONENUMBER("managerPhoneNumber")
}

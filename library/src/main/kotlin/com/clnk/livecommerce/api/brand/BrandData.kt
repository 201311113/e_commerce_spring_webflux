package com.clnk.livecommerce.api.brand

data class CreateBrandReq(
    var name: String,
    var managerName: String,
    var managerPhoneNumber: String
)

data class CreateBrandRes(
    var id: Long = -1
)

data class BrandRes(
    var id: Long = -1,
    var name: String? = null,
    var managerName: String? = null,
    var managerPhoneNumber: String? = null
)

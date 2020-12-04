package com.clnk.livecommerce.api.product

data class CreateOptionReq(
    var title: String,
    var isRequired: Boolean? = false,
    var sortPosition: Int? = null,
    var options: MutableList<OptionItemReq> = mutableListOf()
)

data class CreateOptionRes(
    var id: Long = -1
)

data class OptionItemReq(
    var name: String,
    var sortPosition: Int
)

data class OptionGroupRes(
    var id: Long = -1,
    var title: String? = null,
    var isRequired: Boolean? = null,
    var sortPosition: Int? = null,
    var options: MutableList<OptionItemRes> = mutableListOf()
)

data class OptionItemRes(
    var id: Long = -1,
    var name: String? = null,
    var sortPosition: Int? = null
)


data class OptionGroupListRes(
    var content: MutableList<OptionGroupRes> = mutableListOf()
)

data class UpdateOptionGroupReq(
    var id: Long = -1,
    var title: String? = null,
    var isRequired: Boolean? = false,
    var sortPosition: Int = 0
)

data class UpdateOptionGroupSortReq(
    var optionGroups: MutableList<UpdateOptionGroupReq> = mutableListOf()
)
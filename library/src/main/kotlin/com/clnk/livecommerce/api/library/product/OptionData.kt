package com.clnk.livecommerce.api.library.product

data class CreateOptionReq(
    var title: String,
    var isRequired: Boolean? = false,
    var sortPosition: Int? = null,
    var optionItems: MutableList<CreateOptionItemReq> = mutableListOf()
)

data class CreateOptionRes(
    var id: Long = -1
)

data class CreateOptionItemReq(
//    var id: Long = -1,
    var name: String,
    var sortPosition: Int
)

data class UpdateOptionItemReq(
    var id: Long = -1,
    var name: String,
    var sortPosition: Int
)

data class OptionGroupRes(
    var id: Long = -1,
    var title: String? = null,
    var isRequired: Boolean? = null,
    var sortPosition: Int? = null,
    var optionItems: MutableList<OptionItemRes> = mutableListOf()
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


data class UpdateOptionReq(
    var title: String,
    var isRequired: Boolean? = false,
    var sortPosition: Int? = null,
    var updatedOptionItems: MutableList<UpdateOptionItemReq> = mutableListOf(),
    var newOptionItems: MutableList<CreateOptionItemReq> = mutableListOf(),
    var deletedOptionItemIds: MutableList<Long> = mutableListOf()
)
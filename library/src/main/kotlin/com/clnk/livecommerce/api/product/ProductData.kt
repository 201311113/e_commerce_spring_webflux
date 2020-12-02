package com.cucurbita.api.mentoitem

import com.cucurbita.api.category.MentoItemCategoryRes
import com.cucurbita.api.media.MediaRes
import com.cucurbita.api.mento.MentoRes
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.codec.multipart.FilePart
import java.math.BigDecimal
import java.time.Instant
import javax.validation.constraints.NotBlank

data class CreateMentoItemReq(
    @get:NotBlank
    var title: String,
    var stock: Int,
    var description: String,
    var startedAt: Instant?,
    var endedAt: Instant?,
    var hashTags: String? = null,
    var itemImage: FilePart? = null,
    var categoryRoot: Long? = null,
    var categorySub: Long? = null,
)

class CreateMentoItemRes(var id: Long = -1)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class MentoItemRes(
    var id: Long = -1,
    var title: String? = null,
    var status: ItemStatus? = null,
    var sellPrice: BigDecimal? = null,
    var stock: Int? = null,
    var description: String? = null,
    var startedAt: Instant? = null,
    var endedAt: Instant? = null,
    var mento: MentoRes? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var createdAt: Instant? = null,
    var categorys: MutableList<MentoItemCategoryRes>? = mutableListOf(),
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class MentoItemResForList(
    var id: Long = -1,
    var title: String? = null,
    var status: ItemStatus? = null,
    var sellPrice: BigDecimal? = null,
    var stock: Int? = null,
    var description: String? = null,
    var startedAt: Instant? = null,
    var endedAt: Instant? = null,
    var mento: MentoResForList? = null,
    var medias: MutableList<MediaRes> = mutableListOf(),
    var createdAt: Instant? = null,
    var categorys: MutableList<MentoItemCategoryRes>? = mutableListOf(),
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class MentoResForList(
    var id: Long = -1,
    var member: MemberResList? = null,
    var createdAt: Instant? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class MemberResList(
    var id: Long = -1,
    var snsId: String? = null,
    var nickName: String? = null,
    var realName: String? = null,
    var createdAt: Instant? = null,
)

data class MentoItemStatusRes(
    var id: Long = -1,
    var status: ItemStatus? = null,
)


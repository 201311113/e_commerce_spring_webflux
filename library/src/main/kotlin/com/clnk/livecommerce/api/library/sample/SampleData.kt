package com.clnk.livecommerce.api.library.sample

import java.time.Instant
import javax.validation.constraints.NotBlank

data class CreateSampleReq(
    @field:NotBlank
    var title: String,
    var status: SampleStatus
)

class CreateSampleRes(var id: Long = -1)

data class SampleRes(
    var Id: Long = -1,
    var title: String = "",
    var status: SampleStatus? = null,
    var createdAt: Instant? = null

)


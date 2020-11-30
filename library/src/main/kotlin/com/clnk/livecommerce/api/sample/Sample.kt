package com.clnk.livecommerce.api.sample

import com.clnk.livecommerce.api.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank

@Entity
class Sample(
    @Column(length = 100)
    @NotBlank
    var title: String,
    @Enumerated(EnumType.STRING)
    var status: SampleStatus = SampleStatus.INIT,

    ) : BaseEntity()

enum class SampleStatus {
    INIT, DOING, DONE
}
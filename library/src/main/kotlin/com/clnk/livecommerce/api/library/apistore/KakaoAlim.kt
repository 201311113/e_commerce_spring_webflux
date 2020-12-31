package com.clnk.livecommerce.api.library.apistore

import com.clnk.livecommerce.api.library.model.BaseEntity
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity

@Entity
class KakaoAlim(
    var phone: String,
    var callback: String,
    var msg: String,
    var templateCode: String,
    var failedType: String,
    var failedSubject: String,
    var failedMsg: String,
    var cmid: String? = null,
    var resultCode: String? = null,
    var resultMessage: String? = null
) : BaseEntity()

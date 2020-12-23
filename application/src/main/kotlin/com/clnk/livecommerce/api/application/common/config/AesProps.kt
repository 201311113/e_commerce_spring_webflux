package com.clnk.livecommerce.api.application.common.config

import com.clnk.livecommerce.api.support.AesAttributes
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "aes")
data class AesProps(
    override val secretKey: String,
) : AesAttributes

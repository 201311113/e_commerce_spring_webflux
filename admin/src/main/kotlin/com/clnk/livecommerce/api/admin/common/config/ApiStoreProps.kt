package com.clnk.livecommerce.api.admin.common.config

import com.clnk.livecommerce.api.library.apistore.ApiStoreAttributes
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "apistore")
data class ApiStoreProps(
    override val kakaoEndpoint: String,
    override val appId: String,
    override val apiKey: String
) : ApiStoreAttributes

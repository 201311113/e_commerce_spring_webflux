package com.clnk.livecommerce.api.application.common.config

import com.clnk.livecommerce.api.library.firebase.FirebaseAttributes
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "firebase")
data class FirebaseProps(
    override val account: String
) : FirebaseAttributes

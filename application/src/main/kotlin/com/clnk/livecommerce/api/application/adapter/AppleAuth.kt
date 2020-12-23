package com.clnk.livecommerce.api.application.adapter

import com.auth0.jwt.JWT
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class AppleAuth : SnsAuthAdapter {
    override fun verifyAccessToken(accessToken: String, snsId: String): Boolean {
        val jwt = JWT.decode(accessToken)
        return snsId == jwt.subject
    }
}

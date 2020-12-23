package com.clnk.livecommerce.api.application.adapter

import com.clnk.livecommerce.api.application.common.config.AppProperties
import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.apache.commons.codec.binary.Hex
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val logger = KotlinLogging.logger {}

@Component
class FacebookAuth(
    val appProperties: AppProperties,
    val restTemplate: RestTemplate
) : SnsAuthAdapter {
    override fun verifyAccessToken(accessToken: String, snsId: String): Boolean {
        val resAppToken = restTemplate.getForObject("${appProperties.facebookEndpoint}/oauth/access_token?client_id=${appProperties.facebookAppId}&client_secret=${appProperties.facebookAppSecret}&grant_type=client_credentials",
            FacebookAppAccessToken::class.java
        ) ?: return false
        val appsecretProof = createSignature(resAppToken.accessToken, appProperties.facebookAppId)
        val resUserTokenDebug = restTemplate.getForObject("${appProperties.facebookEndpoint}/v7.0/debug_token?input_token=${accessToken}&access_token=${resAppToken.accessToken}&appsecret_proof=${appsecretProof}",
            FacebookUserAccessToken::class.java
        ) ?: return false
        return snsId == resUserTokenDebug.data.userId
    }

    private fun createSignature(data: String, key: String): String {
        val sha256Hmac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
        sha256Hmac.init(secretKey)
        return Hex.encodeHexString(sha256Hmac.doFinal(data.toByteArray(charset("UTF8"))))
    }

}


data class FacebookAppAccessToken(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String
)

data class FacebookUserAccessToken(
    val data: DebugUserToken
)


data class DebugUserToken(
    @JsonProperty("app_id")
    val appId: String,
    @JsonProperty("user_id")
    val userId: String
)
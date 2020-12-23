package com.clnk.livecommerce.api.application.adapter

import com.clnk.livecommerce.api.application.common.config.AppProperties
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*


private val logger = KotlinLogging.logger {}

@Component
class GoogleAuth(
    val appProperties: AppProperties
) : SnsAuthAdapter {
    val transport = NetHttpTransport()
    val jsonFactory = JacksonFactory.getDefaultInstance()
    val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Arrays.asList(appProperties.googleAppIdIos, appProperties.googleAppIdAnd))
        .build()

    override fun verifyAccessToken(accessToken: String, snsId: String): Boolean {
        logger.debug { "]-----] GoogleAuthTest::verifyAccessToken.accessToken [-----[ ${accessToken}" }
        val idToken = verifier.verify(accessToken) ?: return false
        logger.debug { "]-----] GoogleAuthTest::verifyAccessToken.idToken [-----[ ${idToken}" }
        val payload = idToken.getPayload();
        val userId = payload.subject
        return snsId == userId
    }
}

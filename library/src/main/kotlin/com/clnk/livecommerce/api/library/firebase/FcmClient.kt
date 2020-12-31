package com.clnk.livecommerce.api.library.firebase

import com.fasterxml.jackson.core.type.TypeReference
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

interface FirebaseAttributes {
    val account: String
}

@Component
class FcmClient (@Autowired props: FirebaseAttributes) {
    init {
        val serviceAccount = TypeReference::class.java.getResourceAsStream("/firebase/${props.account}")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://beautalk.firebaseio.com")
            .build()
        FirebaseApp.initializeApp(options)
    }

    fun send(deviceId: String, title: String, body: String, data: Map<String,String>? = null): String? {
        val message = Message.builder()
            .setToken(deviceId)
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )

        if (data != null) {
            message.putAllData(data)
        }

        return try {
            FirebaseMessaging.getInstance()
            .send(message.build())
        } catch(e: FirebaseMessagingException) {
            log.error { "Fail to send a Firebase Cloud message to the device(id=$deviceId) about $title. ${e.message}" }
            return null
        }
    }
}
package com.clnk.livecommerce.api.library.infra

import com.clnk.livecommerce.api.library.firebase.FcmClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Notifier @Autowired constructor(
    private val fcmClient: FcmClient
) {
    fun sendStartDeliveryStartPushToBuyer(deviceId: String, productName: String) {
        val title = "배송시작 안내"
        val body = "구매하신 ${productName}가 배송이 시작되었습니다."

        fcmClient.send(deviceId, title, body)
    }
}
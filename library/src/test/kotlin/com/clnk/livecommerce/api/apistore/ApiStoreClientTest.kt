package com.clnk.livecommerce.api.apistore

import com.clnk.livecommerce.api.LibraryTests
import com.clnk.livecommerce.api.library.apistore.ApiStoreClient
import com.clnk.livecommerce.api.library.apistore.SendAlimtalkReq
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


private val logger = KotlinLogging.logger {}

class ApiStoreClientTest(@Autowired private val client: ApiStoreClient): LibraryTests() {
    @Test
    fun sendAlimtalk() {
        val req = SendAlimtalkReq(
            phone = "01030982101",
            callback = "01030982101",
            msg = "234567",
            templateCode = "vreg001",
        )
        val res = client.sendAlimtalk(req)
        println(res)
    }

}

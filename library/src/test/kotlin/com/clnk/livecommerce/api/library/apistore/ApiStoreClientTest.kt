package com.clnk.livecommerce.api.library.apistore

import com.clnk.livecommerce.api.library.LibraryTests
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


private val logger = KotlinLogging.logger {}

class ApiStoreClientTest(@Autowired private val client: ApiStoreClient) : LibraryTests() {
//    @Test
    fun sendAlimtalk() {
        val req = SendAlimtalkReq(
            phone = "01030982101",
            callback = "01071648767",
            msg = "[뷰톡] 가입 문자 인증 234567를 인증번호 창에 입력해주세요!",
            templateCode = "vreg001",
            failedType = "LMS",
            failedSubject = "뷰톡 핸드폰 인증 입니다.",
            failedMsg = "234567"
        )
        val res = client.sendAlimtalk(req)
        println(res)
    }

}

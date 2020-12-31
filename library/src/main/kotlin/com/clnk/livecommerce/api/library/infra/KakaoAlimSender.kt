package com.clnk.livecommerce.api.library.infra

import com.clnk.livecommerce.api.library.apistore.ApiStoreClient
import com.clnk.livecommerce.api.library.apistore.KakaoAlim
import com.clnk.livecommerce.api.library.apistore.SendAlimtalkReq
import com.clnk.livecommerce.api.library.apistore.repository.KakaoAlimRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class KakaoAlimSender @Autowired constructor(
    private val apiStoreClient: ApiStoreClient,
    private val kakaoAlimRepository: KakaoAlimRepository,
    private var modelMapper: ModelMapper
) {
    fun sendPhoneNumberVerifyCode(phoneNumber: String, certCode: String) {
        val req = SendAlimtalkReq(
            phone = phoneNumber,
            callback = "01071648767",
            msg = "[뷰톡] 가입 문자 인증 ${certCode}를 인증번호 창에 입력해주세요!",
            templateCode = "vreg001",
            failedType = "LMS",
            failedSubject = "뷰톡 핸드폰 인증 입니다.",
            failedMsg = "[뷰톡] 가입 문자 인증 ${certCode}를 인증번호 창에 입력해주세요!"
        )
        val res = apiStoreClient.sendAlimtalk(req)
        val newKakaoAlim = modelMapper.map(req, KakaoAlim::class.java)
        newKakaoAlim.resultCode = res!!.resultCode
        newKakaoAlim.resultMessage = res.resultMessage
        newKakaoAlim.cmid = res.cmid
        kakaoAlimRepository.save(newKakaoAlim)
    }
}
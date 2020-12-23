package com.clnk.livecommerce.api.library.apistore

import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate


interface ApiStoreAttributes {
    val kakaoEndpoint: String
    val appId: String
    val apiKey: String
}

private val logger = KotlinLogging.logger {}

/**
 * 참고 문서 : https://www.apistore.co.kr/api/apiView.do?service_seq=558
 */
@Component
class ApiStoreClient @Autowired constructor(
    private val props: ApiStoreAttributes,
    private val restTemplate: RestTemplate
) {
    /**
     * 알림톡 발송
     */
    fun sendAlimtalk(req: SendAlimtalkReq): SendAlimtalkRes? {
        return try {
            val headers = getHttpHeaders()
            headers["x-waple-authorization"] = props.apiKey
            headers["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8"
            val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
            parameters.add("phone", req.phone)
            parameters.add("callback", req.callback)
            parameters.add("msg", req.msg)
            parameters.add("template_code", req.templateCode)

            req.reqdate?.also { parameters.add("reqdate", it) }
            req.failedType?.also { parameters.add("failed_type", it) }
            req.failedSubject?.also { parameters.add("failed_subject", it) }
            req.failedMsg?.also { parameters.add("failed_msg", it) }
            req.btnTypes?.also { parameters.add("btn_types", it) }
            req.btnTxts?.also { parameters.add("btn_txts", it) }
            req.btnUrls1?.also { parameters.add("btn_urls1", it) }
            req.btnUrls2?.also { parameters.add("btn_urls2", it) }

            val entity = HttpEntity(parameters, headers)

            val url = props.kakaoEndpoint + "/kko/1/msg/${props.appId}"
            restTemplate.postForEntity(url, entity, SendAlimtalkRes::class.java).body!!
        } catch (error: Exception) {
            logger.error("]-----] ApiStoreClient::sendAlimtalk.error [-----[ {}", error)
            null
        }
    }

    private fun getHttpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }
    }

}

data class SendAlimtalkReq(
    /** 수신할 핸드폰 번호 */
    var phone: String,
    /** 발신자 전화번호 */
    var callback: String = "010-3098-2101",
    /** 전송할 메세지 */
    var msg: String,
    /** 카카오 알림톡 템플릿 코드 */
    var templateCode: String,
    /** 발송시간(없을 경우 즉시 발송) */
    var reqdate: String? = null,
    /** 카카오 알림톡 전송 실패 시 전송할 메시지 타입(예시: LMS) */
    var failedType: String? = null,
    /** 카카오 알림톡 전송 실패 시 전송할 제목 */
    var failedSubject: String? = null,
    /** 카카오 알림톡 전송 실패 시 전송할 내용 */
    var failedMsg: String? = null,
    /** 버튼이 여러개일때 버튼타입배열 ,(콤마)로 구분합니다. */
    var btnTypes: String? = null,
    /** 버튼이 여러개일때 버튼명배열 ,(콤마)로 구분합니다. */
    var btnTxts: String? = null,
    /** 버튼이 여러개일때 URL1배열 ,(콤마)로 구분합니다. */
    var btnUrls1: String? = null,
    /** 버튼이 여러개일때 URL2배열 ,(콤마)로 구분합니다. */
    var btnUrls2: String? = null

)

data class SendAlimtalkRes(
    @JsonProperty("result_code")
    var resultCode: String = "",
    @JsonProperty("result_message")
    var resultMessage: String = "",
    var cmid: String = "",
)

package com.clnk.livecommerce.api.application.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {
    @Value("\${sns.facebook.app.id}")
    lateinit var facebookAppId: String

    @Value("\${sns.facebook.app.secret}")
    lateinit var facebookAppSecret: String

    @Value("\${sns.facebook.endpoint}")
    lateinit var facebookEndpoint: String

    @Value("\${sns.kakao.app.id}")
    lateinit var kakaoAppId: String

    @Value("\${sns.kakao.endpoint}")
    lateinit var kakaoEndpoint: String

    @Value("\${sns.google.app.id.and}")
    lateinit var googleAppIdAnd: String

    @Value("\${sns.google.app.id.ios}")
    lateinit var googleAppIdIos: String


}
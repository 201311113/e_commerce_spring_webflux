package com.cucurbita.api.application.mentoitem

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class MentoItemRouter {
    val basePath = "/api/v1/mentoitem"

    @Bean
    fun mentoItemRoute(handler: MentoItemHandler) = coRouter {
        path(basePath).nest {
            POST("", accept(MediaType.MULTIPART_FORM_DATA), handler::create)
            GET("/mine/all",handler::mine)
        }
    }
}
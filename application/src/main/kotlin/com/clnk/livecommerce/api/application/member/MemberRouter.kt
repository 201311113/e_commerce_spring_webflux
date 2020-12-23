package com.clnk.livecommerce.api.application.member

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class MemberRouter {
    val basePath = "/api/v1/member"

    @Bean
    fun memberRoute(handler: MemberHandler) = coRouter {
        path(basePath).nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/me", handler::findMe)
            }
        }
    }
}
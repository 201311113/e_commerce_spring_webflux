package com.clnk.livecommerce.api.application.broadcast

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class BroadcastRouter {
    val basePath = "/api/v1/broadcast"

    @Bean
    fun broadcastRoute(handler: BroadcastHandler) = coRouter {
        path(basePath).nest {
            GET("", handler::findAll)
            GET("/{id}", handler::findById)
        }
    }
}
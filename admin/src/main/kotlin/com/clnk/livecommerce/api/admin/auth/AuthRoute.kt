package com.clnk.livecommerce.api.admin.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class AuthRouter {
    val basePath = "/admin/v1/auth"

    @Bean
    fun authRoute(handler: AuthHandler) = coRouter {
        path(basePath).nest {
            GET("/signin", handler::signin)
            GET("/isauth", handler::isAuth)
        }
    }
}
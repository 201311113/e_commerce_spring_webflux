package com.clnk.livecommerce.api.admin.brand

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class BrandRouter {
    val basePath = "/admin/v1/brand"

    @Bean
    fun brandRoute(handler: BrandHandler) = coRouter {
        path(basePath).nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("", handler::create)
                GET("", handler::findAll)
                GET("/{id}", handler::findById)
                POST("/{id}", handler::update)
            }
        }
    }

}
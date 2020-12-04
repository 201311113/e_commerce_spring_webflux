package com.clnk.livecommerce.api.admin.product

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ProductRouter {
    val basePath = "/admin/v1/product"

    @Bean
    fun productRoute(handler: ProductHandler) = coRouter {
        path(basePath).nest {
            POST("", accept(MediaType.MULTIPART_FORM_DATA), handler::create)
            GET("", handler::findAll)
            GET("/{id}", handler::findById)
            POST("/{id}", handler::update)
        }
    }
}
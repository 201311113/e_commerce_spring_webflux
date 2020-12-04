package com.clnk.livecommerce.api.admin.product

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class OptionRouter {
    val basePath = "/admin/v1/option"

    @Bean
    fun optionRoute(handler: OptionHandler) = coRouter {
        path(basePath).nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("/{productId}", handler::create)
                GET("/{productId}/product", handler::findAllByProductId)
                POST("/update/sort", handler::updateOptionGroupSort)
            }
        }
    }
}
package com.clnk.livecommerce.api.admin.product

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class OptionGroupRouter {
    val basePath = "/admin/v1/optiongroup"

    @Bean
    fun optionGruopRoute(handler: OptionGroupHandler) = coRouter {
        path(basePath).nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("/product/{productId}", handler::create)
                GET("/product/{productId}", handler::findAllByProductId)
                POST("/update/sort", handler::updateOptionGroupSort)
                GET("/{id}", handler::findById)
                POST("/{id}", handler::update)
            }
        }
    }

}
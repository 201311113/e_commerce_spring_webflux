package com.clnk.livecommerce.api.admin.onsaleitem

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class OnSaleItemRouter {
    val basePath = "/admin/v1/onsaleitem"

    @Bean
    fun onSaleItemRoute(handler: OnSaleItemHandler) = coRouter {
        path(basePath).nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("", handler::create)
            }
        }
    }

}
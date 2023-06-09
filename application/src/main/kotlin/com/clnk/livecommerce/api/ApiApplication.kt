package com.clnk.livecommerce.api

import com.clnk.livecommerce.api.application.common.config.AppProperties
import com.clnk.livecommerce.api.application.common.config.AwsProps
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.reactive.config.EnableWebFlux

//@EnableConfigurationProperties(AwsProps::class, AppProperties::class)
@EnableAsync
@EnableWebFlux
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan("com.clnk.livecommerce.api.application.common.config", "com.clnk.livecommerce.api.config")
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}

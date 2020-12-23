package com.clnk.livecommerce.api

import com.clnk.livecommerce.api.admin.common.config.ApiStoreProps
import com.clnk.livecommerce.api.admin.common.config.AwsProps
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.reactive.config.EnableWebFlux

//@EnableConfigurationProperties(AwsProps::class, ApiStoreProps::class)
@EnableAsync
@EnableWebFlux
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan("com.clnk.livecommerce.api.admin.common.config", "com.clnk.livecommerce.api.config")
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}

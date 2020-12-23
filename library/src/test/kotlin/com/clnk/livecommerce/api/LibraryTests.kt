package com.clnk.livecommerce.api

import com.clnk.livecommerce.api.library.apistore.ApiStoreAttributes
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootConfiguration
@ComponentScan
@EnableAutoConfiguration
@ConfigurationPropertiesScan
class TestSpringContext


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [(TestSpringContext::class)])
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaRepositories(basePackages = ["com.clnk.livecommerce.api"])
open class LibraryTests

@TestConfiguration
class ApiStoreProps(
    override val kakaoEndpoint: String = "http://api.apistore.co.kr/",
    override val appId: String = "clnkcompany",
    override val apiKey: String = "01bfea901e090e7a34fc32bbd3bda17671cb8cc5"
): ApiStoreAttributes
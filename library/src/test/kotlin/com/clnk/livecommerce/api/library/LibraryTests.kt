package com.clnk.livecommerce.api.library

import com.clnk.livecommerce.api.library.apistore.ApiStoreAttributes
import com.clnk.livecommerce.api.library.config.AwsAttributes
import com.clnk.livecommerce.api.library.firebase.FirebaseAttributes
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
    override val apiKey: String = "ODk2Ni0xNTMyOTE3ODIzNTk4LTY3YzMyYTBlLWY5N2UtNGRkZi04MzJhLTBlZjk3ZTRkZGZlYg=="
): ApiStoreAttributes

@TestConfiguration
class AwsProps(
    override val accessKey: String = "",
    override val secretKey: String = "",
    override val bucketName: String = "",
    override val bucketUrl: String = "",
    override val tempFilePath: String = ""
) : AwsAttributes

@TestConfiguration
class FirebaseProps(
    override val account: String = "BeautalkAccountKey.json"
): FirebaseAttributes

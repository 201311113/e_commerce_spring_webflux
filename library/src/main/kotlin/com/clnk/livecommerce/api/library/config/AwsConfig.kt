package com.clnk.livecommerce.api.library.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

interface AwsAttributes {
    val accessKey: String
    val secretKey: String
    val bucketName: String
    val bucketUrl: String
    val tempFilePath: String
}

@Configuration
class AwsConfig(private val props: AwsAttributes) {
    @Bean
    fun credentials(): BasicAWSCredentials {
        return BasicAWSCredentials(props.accessKey, props.secretKey)
    }

    @Bean
    fun amazonS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials()))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build()

    }
}
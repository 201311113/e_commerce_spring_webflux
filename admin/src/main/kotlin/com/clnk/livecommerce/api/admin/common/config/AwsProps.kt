package com.clnk.livecommerce.api.admin.common.config

import com.clnk.livecommerce.api.library.config.AwsAttributes
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "aws")
data class AwsProps(
    override val accessKey: String,
    override val secretKey: String,
    override val bucketName: String,
    override val bucketUrl: String,
    override val tempFilePath: String
) : AwsAttributes

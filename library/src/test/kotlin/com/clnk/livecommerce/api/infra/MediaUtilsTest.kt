package com.clnk.livecommerce.api.infra

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.codec.multipart.FilePart
import org.springframework.test.context.ActiveProfiles
import java.io.File

private val logger = KotlinLogging.logger {}
@ActiveProfiles("local")
internal class MediaUtilsTest{
    @Test
    fun fileTransfer() {
        val file = File("/Users/cola/Downloads/colacan.png")
        val tempFile = File("/Users/cola/Downloads/tmp/colacan.png")
        file.copyTo(tempFile)

        assertTrue(1 > 0)
    }
}
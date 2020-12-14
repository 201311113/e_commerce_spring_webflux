package com.clnk.livecommerce.api.infra

import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

@ActiveProfiles("local")
internal class MediaUtilsTest {
    @Test
    fun fileTransfer() {
        val file = File("/Users/cola/Downloads/colacan.png")
        val tempFile = File("/Users/cola/Downloads/tmp/colacan.png")
        file.copyTo(tempFile)

        assertTrue(1 > 0)
    }

    @Test
    fun uuidGenerate() {
        val mediaUuid = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(Instant.now()) + RandomStringUtils.randomAlphabetic(16)
        val mediaUuidSimple = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault()).format(Instant.now()) + RandomStringUtils.randomAlphabetic(4)
        logger.debug { "]-----] MediaUtilsTest::uuidGenerate mediaUuid[-----[ ${mediaUuid}" }
        logger.debug { "]-----] MediaUtilsTest::uuidGenerate mediaUuid[-----[ ${mediaUuid.length}" }
        logger.debug { "]-----] MediaUtilsTest::uuidGenerate mediaUuid[-----[ ${mediaUuidSimple.length}" }
    }
}
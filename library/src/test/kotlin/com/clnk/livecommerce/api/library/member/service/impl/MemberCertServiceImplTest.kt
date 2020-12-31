package com.clnk.livecommerce.api.library.member.service.impl

import mu.KotlinLogging
import org.apache.commons.lang3.RandomUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private val logger = KotlinLogging.logger {}

internal class MemberCertServiceImplTest {
    @Test
    fun generateCertNumber() {
        val certNumber = RandomUtils.nextInt(100000, 999999)
        logger.debug { "]-----] MemberCertServiceImplTest::generateCertNumber certNumber[-----[ ${certNumber}" }
        assertTrue(certNumber in 999999 downTo 100000)
    }
}
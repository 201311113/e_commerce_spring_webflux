package com.clnk.livecommerce.api.support

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private val logger = KotlinLogging.logger {}

internal class AES256Test {
    @Test
    fun keyGen() {
        val key1= "k".repeat(32)
        val key2= "asdf".repeat(8)
        logger.debug { "]-----] AES256Test::keyGen key1[-----[ ${key1}" }
        logger.debug { "]-----] AES256Test::keyGen key2[-----[ ${key2}" }
        assertTrue(key1.length == 32)
        assertTrue(key2.length == 32)
    }
    @Test
    fun encrypt() {
        val secretKey = "662ede816988e58fb6d057d9d85605e0"
        val originString = "ttt"
        val encryptString = ChCrypto.aesEncrypt(originString, secretKey)
        logger.debug { "]-----] AES256Test::encrypt encryptString[-----[ ${encryptString}" }
        val decryptString = ChCrypto.aesDecrypt(encryptString, secretKey)
        logger.debug { "]-----] AES256Test::encrypt decryptString[-----[ ${decryptString}" }
        assertTrue(originString == decryptString)

        val encryptStringSec = ChCrypto.aesEncrypt(originString, secretKey)
        logger.debug { "]-----] AES256Test::encrypt encryptStringSec[-----[ ${encryptStringSec}" }
        assertTrue(encryptString == encryptStringSec)
//        QF3EpvZwMVF34r2JMaQtJA==
//        QF3EpvZwMVF34r2JMaQtJA==
    }

    @Test
    fun encryptCBC() {
        val originString = "ttt"
        val encryptString = ChCryptoCBC.aesEncrypt(originString, "k")
        logger.debug { "]-----] AES256Test::encryptCBC encryptString[-----[ ${encryptString}" }
        val decryptString = encryptString?.let { ChCryptoCBC.aesDecrypt(it, "k") }
        logger.debug { "]-----] AES256Test::encryptCBC decryptString[-----[ ${decryptString}" }
        assertTrue(originString == decryptString)

        val encryptStringSec = ChCryptoCBC.aesEncrypt(originString, "k")
        assertTrue(originString != encryptStringSec)
//        1jcEj/LpjLQcUM4EDwO7dK2qnq6VojleyMdTGRP58nI=
//        JXhBSdJm/8j3iVinvptYpTak68zEUxE1GvgT8TJO1ss=

    }
}
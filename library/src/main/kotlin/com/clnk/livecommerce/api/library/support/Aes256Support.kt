package com.clnk.livecommerce.api.support

import java.security.SecureRandom
import java.security.SecureRandom.getSeed
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

// 키 길이는 256 bit(32 byte)이다.
private const val KEY_LENGTH = 32

// 1바이트 문자열이 아닌 경우에는 java.security.InvalidKeyException: Invalid AES key length 오류가 난다. (ex: 한글 등등)
private val KEY = "k".repeat(KEY_LENGTH)

// 첫 블럭과 XOR 연산을 해야되기 때문에 iv의 길이는 블럭 사이즈인 16 byte이다.
private const val BLOCK_SIZE = 16

// PBKDF2에 적용할 값들이다.
private const val SALT = "{,*jbU787l@BcG:L]Oos/?s7EwMo:i/%No2z)sP@ut@=cydO9&@Xa137-ZHkke"
private const val ITERATION_COUNT = 1024

// 키 값이 256 bit이기 때문에 해시 돌린 digest 길이도 256 bit여야 한다.
private const val DIGEST_BIT_LENGTH = 256

interface AesAttributes {
    val secretKey: String
}

class Aes256Support(private val props: AesAttributes)

object ChCrypto {
    @JvmStatic
    fun aesEncrypt(v: String, secretKey: String) = AES256.encrypt(v, secretKey)

    @JvmStatic
    fun aesDecrypt(v: String, secretKey: String) = AES256.decrypt(v, secretKey)
}

private object AES256 {
    private val encorder = Base64.getEncoder()
    private val decorder = Base64.getDecoder()

    private fun cipher(opmode: Int, secretKey: String): Cipher {
        if (secretKey.length != 32) throw RuntimeException("SecretKey length is not 32 chars")
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        val iv = IvParameterSpec(secretKey.substring(0, 16).toByteArray(Charsets.UTF_8))
        c.init(opmode, sk, iv)
        return c
    }

    fun encrypt(str: String, secretKey: String): String {
        val encrypted = cipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(str.toByteArray(Charsets.UTF_8))
        return String(encorder.encode(encrypted))
    }

    fun decrypt(str: String, secretKey: String): String {
        val byteStr = decorder.decode(str.toByteArray(Charsets.UTF_8))
        return String(cipher(Cipher.DECRYPT_MODE, secretKey).doFinal(byteStr))
    }
}


object ChCryptoCBC {
    @JvmStatic
    fun aesEncrypt(v: String, seed: String) = AES256CBC.encrypt(v, seed)

    @JvmStatic
    fun aesDecrypt(v: String, seed: String) = AES256CBC.decrypt(v, seed)
}

private object AES256CBC {
    private val encorder = Base64.getEncoder()
    private val decorder = Base64.getDecoder()

    private fun secretKeySpec(seed: String) = try {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val key = seed.repeat(KEY_LENGTH).substring(0,16)
        val spec = PBEKeySpec(key.toCharArray(), SALT.toByteArray(), ITERATION_COUNT, DIGEST_BIT_LENGTH)
        SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    } catch (e: Exception) {
        println("Error while generating key: $e")
        null
    }

    fun encrypt(plainText: String, seed: String): String? = try {
        val iv = ByteArray(BLOCK_SIZE)
        val secureRandom = SecureRandom.getInstanceStrong()
        secureRandom.nextBytes(iv)
        val ivParameterSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec(seed), ivParameterSpec)
        val encrypted = cipher.doFinal(plainText.toByteArray())
        iv + encrypted
        String(encorder.encode(iv + encrypted))
    } catch (e: Exception) {
        println("Error while encrypting: $e")
        null
    }

    fun decrypt(plainText: String, seed: String): String? = try {
        val cipherText = decorder.decode(plainText.toByteArray(Charsets.UTF_8))
        val iv = cipherText.copyOfRange(0, BLOCK_SIZE)
        val ivParameterSpec = IvParameterSpec(iv)
        val encrypted = cipherText?.copyOfRange(BLOCK_SIZE, cipherText.size)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec(seed), ivParameterSpec)
        String(cipher.doFinal(encrypted))
    } catch (e: Exception) {
        println("Error while decrypting: $e")
        null
    }
}

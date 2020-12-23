package com.clnk.livecommerce.api.application.common.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

@Component
class JWTService(@Value("\${jwt.secret}") val secret: String,
                 @Value("\${jwt.refresh}") val refresh: String) {

    private val FOUR_HOURS: Long = 1000 * 60 * 60 * 4
    private val ONE_DAYS: Long = 1000 * 60 * 60 * 24

    fun accessToken(username: String, roles: Array<String>): String {
        return generate(username, FOUR_HOURS, roles, secret)
    }

    fun decodeAccessToken(accessToken: String): DecodedJWT {
        return decode(secret, accessToken)
    }

    fun refreshToken(username: String, roles: Array<String>): String {
        return generate(username, ONE_DAYS, roles, refresh)
    }

    fun decodeRefreshToken(refreshToken: String): DecodedJWT {
        return decode(refresh, refreshToken)
    }

    fun getRoles(decodedJWT: DecodedJWT) = decodedJWT.getClaim("roles").asList(String::class.java)
        .map { SimpleGrantedAuthority(it) }

    private fun generate(username: String, expirationInMillis: Long, roles: Array<String>, signature: String): String {
        val now = Date()
        val validity = Date(getExpiration(expirationInMillis))
        return JWT.create()
            .withSubject(username)
            .withIssuer("com.clnk.livecommerce")
            .withExpiresAt(Date(System.currentTimeMillis() + expirationInMillis))
            .withArrayClaim("roles", roles)
            .withIssuedAt(now)
            .withExpiresAt(validity)
            .sign(Algorithm.HMAC512(signature.toByteArray()))
    }

    private fun getExpiration(expirationInMillis: Long): Long = Date().toInstant()
        .plus(Duration.ofMillis(expirationInMillis))
        .toEpochMilli()

    private fun decode(signature: String, token: String): DecodedJWT {
        return JWT.require(Algorithm.HMAC512(signature.toByteArray()))
            .withIssuer("com.clnk.livecommerce")
            .build()
            .verify(token.replace("Bearer ", ""))
    }

    fun getAuthentication(accessToken: String): Authentication? {
        val jwt = decode(secret, accessToken)
        val userId = jwt.subject
        val authorities = getRoles(jwt)
        return UsernamePasswordAuthenticationToken(userId, null, authorities)
    }

}
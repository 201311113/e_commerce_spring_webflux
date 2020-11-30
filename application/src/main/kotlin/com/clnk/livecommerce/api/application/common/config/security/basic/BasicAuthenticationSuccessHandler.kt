package com.clnk.livecommerce.api.application.common.config.security.basic

import com.clnk.livecommerce.api.application.common.config.security.JWTService
import com.clnk.livecommerce.api.application.common.exception.HttpExceptionFactory.unauthorized
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

class BasicAuthenticationSuccessHandler(private val jwtService: JWTService) : ServerAuthenticationSuccessHandler {
    private val FOUR_HOURS: Long = 1000 * 60 * 60 * 4
    private val ONE_DAYS: Long = 1000 * 60 * 60 * 24

    override fun onAuthenticationSuccess(webFilterExchange: WebFilterExchange?, authentication: Authentication?): Mono<Void> = mono {
        val principal = authentication?.principal ?: throw unauthorized()
        when (principal) {
            is User -> {
                val roles = principal.authorities.map { it.authority }.toTypedArray()
                val accessToken = jwtService.accessToken(principal.username, FOUR_HOURS, roles)
                val refreshToken = jwtService.refreshToken(principal.username, ONE_DAYS, roles)
                webFilterExchange?.exchange?.response?.headers?.set("Authorization", accessToken)
                webFilterExchange?.exchange?.response?.headers?.set("JWT-Refresh-Token", refreshToken)
                webFilterExchange?.exchange?.response?.headers?.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "${HttpHeaders.AUTHORIZATION}, JWT-Refresh-Token")
            }
        }
        return@mono null
    }
}
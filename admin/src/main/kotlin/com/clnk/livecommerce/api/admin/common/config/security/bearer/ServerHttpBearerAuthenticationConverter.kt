package com.clnk.livecommerce.api.admin.common.config.security.bearer

import com.clnk.livecommerce.api.admin.common.config.security.JWTService
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

class ServerHttpBearerAuthenticationConverter(private val jwtService: JWTService) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> = mono {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return@mono null
        if (!authHeader.startsWith("Bearer ")) {
            return@mono null
        }
        try {
            return@mono jwtService.getAuthentication(authHeader)
        } catch (e: Throwable) {
            log.debug { "]-----] Admin ServerHttpBearerAuthenticationConverter::convert.error [-----[ ${e}" }
            return@mono null
        }
    }
}
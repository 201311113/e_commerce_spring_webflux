package com.clnk.livecommerce.api.admin.auth

import mu.KotlinLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait

private val log = KotlinLogging.logger {}

@Component
class AuthHandler() {
    suspend fun signin(request: ServerRequest): ServerResponse {
        log.debug { "]-----] AuthHandler::signin [-----[ call " }
        return ok().contentType(APPLICATION_JSON).bodyValueAndAwait("Hello World")

    }

    suspend fun isAuth(request: ServerRequest): ServerResponse {
        log.debug { "]-----] AuthHandler::isAuth [-----[ call " }
        return ok().contentType(APPLICATION_JSON).bodyValueAndAwait("isAuth")

    }
}
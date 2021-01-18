package com.clnk.livecommerce.api.application.broadcast

import com.clnk.livecommerce.api.library.broadcast.service.BroadcastService
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class BroadcastHandler(
    private val broadcastService: BroadcastService,
    private val validator: Validator
) {
    suspend fun findAll(request: ServerRequest): ServerResponse {
        val sort = Sort.by(Sort.Direction.DESC, "id")
        val page = if (request.queryParam("page").isPresent) request.queryParam("page").get().toInt() else 0
        val size = if (request.queryParam("size").isPresent) request.queryParam("size").get().toInt() else 10
        return broadcastService.findAll(PageRequest.of(page, size, sort), request.queryParams()).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        return broadcastService.findById(id).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

}
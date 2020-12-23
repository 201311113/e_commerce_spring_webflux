package com.clnk.livecommerce.api.admin.onsaleitem

import com.clnk.livecommerce.api.library.onsaleitem.CreateOnSaleItemReq
import com.clnk.livecommerce.api.onsaleitem.service.OnSaleItemService
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class OnSaleItemHandler(
    private val onSaleItemService: OnSaleItemService,
    private val validator: Validator
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        log.info { "]-----] OnSaleItemHandler::create call [-----[ " }
        val req = request.awaitBody<CreateOnSaleItemReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return onSaleItemService.create(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findAll(request: ServerRequest): ServerResponse {
        val sort = Sort.by(Sort.Direction.DESC, "id")
        val page = if (request.queryParam("page").isPresent) request.queryParam("page").get().toInt() else 0
        val size = if (request.queryParam("size").isPresent) request.queryParam("size").get().toInt() else 20
        return onSaleItemService.findAll(PageRequest.of(page, size, sort), request.queryParams()).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return onSaleItemService.findById(id, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        log.info { "]-----] OnSaleItemHandler::update call [-----[ " }
        val id = request.pathVariable("id").toLong()
        val req = request.awaitBody<CreateOnSaleItemReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return onSaleItemService.update(id, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }
}
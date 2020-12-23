package com.clnk.livecommerce.api.admin.brand

import com.clnk.livecommerce.api.library.brand.CreateBrandReq
import com.clnk.livecommerce.api.library.brand.service.BrandService
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
class BrandHandler(
    private val brandService: BrandService,
    private val validator: Validator
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        log.info { "]-----] BrandHandler::create call [-----[ " }
        val req = request.awaitBody<CreateBrandReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return brandService.create(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findAll(request: ServerRequest): ServerResponse {
        val sort = Sort.by(Sort.Direction.DESC, "id")
        val page = if (request.queryParam("page").isPresent) request.queryParam("page").get().toInt() else 0
        val size = if (request.queryParam("size").isPresent) request.queryParam("size").get().toInt() else 20
        return brandService.findAll(PageRequest.of(page, size, sort), request.queryParams()).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return brandService.findById(id, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        log.info { "]-----] BrandHandler::update call [-----[ " }
        val id = request.pathVariable("id").toLong()
        val req = request.awaitBody<CreateBrandReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return brandService.update(id, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }
}
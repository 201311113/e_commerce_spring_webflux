package com.clnk.livecommerce.api.admin.product

import com.clnk.livecommerce.api.product.CreateOptionReq
import com.clnk.livecommerce.api.product.UpdateOptionGroupSortReq
import com.clnk.livecommerce.api.product.service.OptionService
import mu.KotlinLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class OptionHandler(
    private val optionService: OptionService,
    private val validator: Validator
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionHandler::create call [-----[ " }
        val productId = request.pathVariable("productId").toLong()
        val req = request.awaitBody<CreateOptionReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return optionService.create(productId, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findAllByProductId(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionHandler::findAllByProductId call [-----[ " }
        val productId = request.pathVariable("productId").toLong()
        return optionService.findAllByProductId(productId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun updateOptionGroupSort(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionHandler::updateOptionGroupSort call [-----[ " }
        val req = request.awaitBody<UpdateOptionGroupSortReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return optionService.updateOptionGroupSort(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }
}
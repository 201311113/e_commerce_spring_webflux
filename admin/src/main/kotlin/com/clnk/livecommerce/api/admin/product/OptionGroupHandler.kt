package com.clnk.livecommerce.api.admin.product

import com.clnk.livecommerce.api.product.CreateOptionReq
import com.clnk.livecommerce.api.product.UpdateOptionGroupSortReq
import com.clnk.livecommerce.api.product.UpdateOptionReq
import com.clnk.livecommerce.api.product.service.OptionGroupService
import mu.KotlinLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class OptionGroupHandler(
    private val optionGroupService: OptionGroupService,
    private val validator: Validator
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionGroupHandler::create call [-----[ " }
        val productId = request.pathVariable("productId").toLong()
        val req = request.awaitBody<CreateOptionReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return optionGroupService.create(productId, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findAllByProductId(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionGroupHandler::findAllByProductId call [-----[ " }
        val productId = request.pathVariable("productId").toLong()
        return optionGroupService.findAllByProductId(productId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun updateOptionGroupSort(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionGroupHandler::updateOptionGroupSort call [-----[ " }
        val req = request.awaitBody<UpdateOptionGroupSortReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return optionGroupService.updateOptionGroupSort(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionGroupHandler::findById call [-----[ " }
        val id = request.pathVariable("id").toLong()
        return optionGroupService.findById(id).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionGroupHandler::update call [-----[ " }
        val id = request.pathVariable("id").toLong()
        val req = request.awaitBody<UpdateOptionReq>()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return optionGroupService.update(id, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun delete(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionGroupHandler::delete call [-----[ " }
        val id = request.pathVariable("id").toLong()
        val adminId = request.awaitPrincipal()!!.name.toLong()
        return optionGroupService.delete(id,adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }
}
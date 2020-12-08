package com.clnk.livecommerce.api.admin.onsaleitem

import com.clnk.livecommerce.api.onsaleitem.CreateOnSaleItemReq
import com.clnk.livecommerce.api.onsaleitem.service.OnSaleItemService
import mu.KotlinLogging
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
}
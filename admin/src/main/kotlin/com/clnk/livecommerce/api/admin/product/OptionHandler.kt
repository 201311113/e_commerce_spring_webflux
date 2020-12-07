package com.clnk.livecommerce.api.admin.product

import com.clnk.livecommerce.api.product.service.OptionService
import mu.KotlinLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class OptionHandler(
    private val optionService: OptionService,
    private val validator: Validator
) {
    suspend fun findById(request: ServerRequest): ServerResponse {
        log.info { "]-----] OptionHandler::findById call [-----[ " }
        val id = request.pathVariable("id").toLong()
        return optionService.findById(id).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }
}
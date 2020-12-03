package com.cucurbita.api.application.mentoitem

import com.cucurbita.api.exception.ErrorMessageCode
import com.cucurbita.api.exception.MentoItemException
import com.cucurbita.api.mentoitem.CreateMentoItemReq
import com.cucurbita.api.mentoitem.service.MentoItemService
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.FormFieldPart
import org.springframework.http.codec.multipart.Part
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.time.Instant
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class MentoItemHandler(
    private val mentoItemService: MentoItemService,
    private val validator: Validator
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] MentoHandler::create multipartMap [-----[ ${multipartMap}" }
        val memberId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return mentoItemService.create(req, memberId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

    suspend fun mine(request: ServerRequest): ServerResponse {
        val memberId = request.awaitPrincipal()!!.name.toLong()
        val sort = Sort.by(Sort.Direction.DESC, "id")
        val page = if (request.queryParam("page").isPresent) request.queryParam("page").get().toInt() else 0
        val size = if (request.queryParam("size").isPresent) request.queryParam("size").get().toInt() else 10
        return mentoItemService.findAllByMemberId(PageRequest.of(page, size, sort), memberId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    private fun mapToReq(multipartMap: MultiValueMap<String, Part>): CreateMentoItemReq {
        val partMap: Map<String, Part> = multipartMap.toSingleValueMap()
        var title: String
        if (partMap.containsKey("title")) {
            title = (partMap["title"] as FormFieldPart).value()
            if (title.isBlank()) throw MentoItemException(ErrorMessageCode.MENTOITEM_TITLE_REQUIRED)
        } else {
            throw MentoItemException(ErrorMessageCode.MENTOITEM_TITLE_REQUIRED)
        }

        var stock: Int = 0
        if (partMap.containsKey("stock")) {
            stock = (partMap["stock"] as FormFieldPart).value() as Int
        }

        var description: String = ""
        if (partMap.containsKey("description")) {
            description = (partMap["description"] as FormFieldPart).value()
            if (description.isBlank()) throw MentoItemException(ErrorMessageCode.MENTOITEM_DESCRIPTION_REQUIRED)
        }

        var startedAt: Instant? = null
        if (partMap.containsKey("startedAt")) {
            startedAt = (partMap["startedAt"] as FormFieldPart).value() as Instant
        }
        var endedAt: Instant? = null
        if (partMap.containsKey("endedAt")) {
            endedAt = (partMap["endedAt"] as FormFieldPart).value() as Instant
        }
        var hashTags = ""
        if (partMap.containsKey("hashTags")) {
            hashTags = (partMap["hashTags"] as FormFieldPart).value()
        }

        var itemImage: FilePart
        if (partMap.containsKey("itemImage")) {
            val itemImagePart = partMap["itemImage"]
            log.debug { "]-----] MentoItemHandler::validate itemImagePart [-----[ ${itemImagePart}" }
            if (itemImagePart is FilePart) {
                itemImage = itemImagePart
            } else {
                throw MentoItemException(ErrorMessageCode.MENTOITEM_IMAGE_REQUIRED)
            }
        } else {
            throw MentoItemException(ErrorMessageCode.MENTOITEM_IMAGE_REQUIRED)
        }

        var categoryRoot: Long? = null
        if (partMap.containsKey("categoryRoot")) {
            categoryRoot = (partMap["categoryRoot"] as FormFieldPart).value().toLong()
        }

        var categorySub: Long? = null
        if (partMap.containsKey("categorySub")) {
            categorySub = (partMap["categorySub"] as FormFieldPart).value().toLong()
        }
        return CreateMentoItemReq(
            title = title,
            stock = stock,
            description = description,
            startedAt = startedAt,
            endedAt = endedAt,
            hashTags = hashTags,
            itemImage = itemImage,
            categoryRoot = categoryRoot,
            categorySub = categorySub
        )
    }
}
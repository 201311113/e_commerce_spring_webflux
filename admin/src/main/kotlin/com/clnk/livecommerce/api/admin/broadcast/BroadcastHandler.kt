package com.clnk.livecommerce.api.admin.broadcast

import com.clnk.livecommerce.api.broadcast.BroadcastReq
import com.clnk.livecommerce.api.broadcast.service.BroadcastService
import com.clnk.livecommerce.api.exception.BroadcastException
import com.clnk.livecommerce.api.exception.ErrorMessageCode
import com.clnk.livecommerce.api.media.MediaReq
import com.clnk.livecommerce.api.media.NewImage
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
class BroadcastHandler(
    private val broadcastService: BroadcastService,
    private val validator: Validator
) {
    suspend fun create(request: ServerRequest): ServerResponse {
        log.info { "]-----] BroadcastHandler::create call [-----[" }
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] BroadcastHandler::create multipartMap [-----[ ${multipartMap}" }
        val adminId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return broadcastService.create(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

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

    suspend fun update(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] BroadcastHandler::update multipartMap [-----[ ${multipartMap}" }
        val adminId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return broadcastService.update(id, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

    private fun mapToReq(multipartMap: MultiValueMap<String, Part>): BroadcastReq {
        log.debug { "]-----] BroadcastHandler::create mapToReq [-----[ ${multipartMap}" }
        val partMap: Map<String, Part> = multipartMap.toSingleValueMap()
        var title: String = ""
        if (partMap.containsKey("title")) {
            title = (partMap["name"] as FormFieldPart).value()
            if (title.isBlank()) throw BroadcastException(ErrorMessageCode.BROADCAST_TITLE_REQUIRED)
        } else {
            throw BroadcastException(ErrorMessageCode.BROADCAST_TITLE_REQUIRED)
        }

        var description: String = ""
        if (partMap.containsKey("description")) {
            description = (partMap["description"] as FormFieldPart).value()
            if (description.isBlank()) throw BroadcastException(ErrorMessageCode.BROADCAST_DESCRIPTION_REQUIRED)
        } else {
            throw BroadcastException(ErrorMessageCode.BROADCAST_DESCRIPTION_REQUIRED)
        }

        var startAt: Instant
        if (partMap.containsKey("startAt")) {
            val startAtStr = (partMap["startAt"] as FormFieldPart).value()
            startAt = Instant.parse(startAtStr)
            if (startAtStr.isBlank()) throw BroadcastException(ErrorMessageCode.BROADCAST_STARTAT_REQUIRED)
        } else {
            throw BroadcastException(ErrorMessageCode.BROADCAST_STARTAT_REQUIRED)
        }

        var endAt: Instant
        if (partMap.containsKey("endAt")) {
            val endAtStr = (partMap["endAt"] as FormFieldPart).value()
            endAt = Instant.parse(endAtStr)
            if (endAtStr.isBlank()) throw BroadcastException(ErrorMessageCode.BROADCAST_ENDAT_REQUIRED)
        } else {
            throw BroadcastException(ErrorMessageCode.BROADCAST_ENDAT_REQUIRED)
        }

        var onSaleItemIds: MutableList<Long>
        if (partMap.containsKey("onSaleItemIds")) {
            val onSaleItemIdsStr = (partMap["onSaleItemIds"] as FormFieldPart).value()
            if (onSaleItemIdsStr.isBlank()) throw BroadcastException(ErrorMessageCode.BROADCAST_ONSALEITEMS_REQUIRED)
            onSaleItemIds = onSaleItemIdsStr.split(",").map { it.toLong() }.toMutableList()
        } else {
            throw BroadcastException(ErrorMessageCode.BROADCAST_ONSALEITEMS_REQUIRED)
        }

        var deletedOnSaleItemIds: MutableList<Long>
        if (partMap.containsKey("deletedOnSaleItemIds")) {
            val deletedOnSaleItemIdsStr = (partMap["deletedOnSaleItemIds"] as FormFieldPart).value()
            if (deletedOnSaleItemIdsStr.isBlank()) throw BroadcastException(ErrorMessageCode.BROADCAST_ONSALEITEMS_REQUIRED)
            deletedOnSaleItemIds = deletedOnSaleItemIdsStr.split(",").map { it.toLong() }.toMutableList()
        } else {
            throw BroadcastException(ErrorMessageCode.BROADCAST_ONSALEITEMS_REQUIRED)
        }

        val updatedImages: MutableList<MediaReq> = mutableListOf()
        val newImages: MutableList<NewImage> = mutableListOf()

        if (partMap.containsKey("imageCount")) {
            val imageCount = (partMap["imageCount"] as FormFieldPart).value().toInt()
            if (imageCount < 0) throw BroadcastException(ErrorMessageCode.PRODUCT_IMAGE_REQUIRED)
            for (i in 0 until imageCount) {
                val mediaId = (partMap["image[$i].id"] as FormFieldPart).value()
                val imagePart = partMap["image[$i]"]
                if (imagePart is FilePart) {
                    newImages.add(
                        NewImage(
                            newImage = imagePart,
                            sortPosition = i
                        )
                    )
                } else {
                    updatedImages.add(
                        MediaReq(
                            id = mediaId.toLong(),
                            sortPosition = i
                        )
                    )
                }

            }
        } else {
            throw BroadcastException(ErrorMessageCode.PRODUCT_IMAGE_REQUIRED)
        }

        val deletedImages: MutableList<Long> = mutableListOf()
        if (multipartMap.containsKey("deletedImages")) {
            val deletedImagesMap = multipartMap["deletedImages"]
            if (deletedImagesMap!!.size > 0) {
                deletedImagesMap.map {
                    val careerContent = (it as FormFieldPart).value()
                    if (careerContent.isNotBlank()) {
                        deletedImages.add(careerContent.toLong())
                    }
                }
            }
        }
        return BroadcastReq(
            title = title,
            description = description,
            startAt = startAt,
            endAt = endAt,
            onSaleItemIds = onSaleItemIds,
            deletedOnSaleItemIds = deletedOnSaleItemIds,
            updatedImages = updatedImages,
            newImages = newImages,
            deletedImages = deletedImages
        )
    }
}
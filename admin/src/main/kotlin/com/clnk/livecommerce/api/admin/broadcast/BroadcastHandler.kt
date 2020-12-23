package com.clnk.livecommerce.api.admin.broadcast

import com.clnk.livecommerce.api.library.broadcast.BroadcastReq
import com.clnk.livecommerce.api.library.broadcast.service.BroadcastService
import com.clnk.livecommerce.api.library.exception.BroadcastException
import com.clnk.livecommerce.api.library.exception.ErrorMessageCode
import com.clnk.livecommerce.api.library.media.MediaReq
import com.clnk.livecommerce.api.library.media.NewImage
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
            title = (partMap["title"] as FormFieldPart).value()
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

        var onSaleItemIds: MutableList<Long> = mutableListOf()
        if (multipartMap.containsKey("onSaleItemId")) {
            val onSaleItemIdsMap = multipartMap["onSaleItemId"]
            if (onSaleItemIdsMap!!.size > 0) {
                onSaleItemIdsMap.map {
                    val onSaleItemId = (it as FormFieldPart).value()
                    if (onSaleItemId.isNotBlank()) {
                        onSaleItemIds.add(onSaleItemId.toLong())
                    }
                }
            }
        }

        var deletedOnSaleItemIds: MutableList<Long> = mutableListOf()
        if (multipartMap.containsKey("deletedOnSaleItemId")) {
            val deletedOnSaleItemIdsMap = multipartMap["deletedOnSaleItemId"]
            if (deletedOnSaleItemIdsMap!!.size > 0) {
                deletedOnSaleItemIdsMap.map {
                    val deletedOnSaleItemId = (it as FormFieldPart).value()
                    if (deletedOnSaleItemId.isNotBlank()) {
                        deletedOnSaleItemIds.add(deletedOnSaleItemId.toLong())
                    }
                }
            }
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
                    val deletedImage = (it as FormFieldPart).value()
                    if (deletedImage.isNotBlank()) {
                        deletedImages.add(deletedImage.toLong())
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
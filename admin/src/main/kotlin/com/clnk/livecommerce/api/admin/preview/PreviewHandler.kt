package com.clnk.livecommerce.api.admin.preview

import com.clnk.livecommerce.api.library.exception.BroadcastException
import com.clnk.livecommerce.api.library.exception.ErrorMessageCode
import com.clnk.livecommerce.api.library.exception.PreviewException
import com.clnk.livecommerce.api.library.media.MediaReq
import com.clnk.livecommerce.api.library.media.NewImage
import com.clnk.livecommerce.api.library.preview.CreatePreviewReq
import com.clnk.livecommerce.api.library.preview.service.PreviewService
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
class PreviewHandler(
    private val previewService: PreviewService,
    private val validator: Validator
) {

    suspend fun create(request: ServerRequest): ServerResponse {
        log.info { "]-----] PreviewHandler::create call [-----[" }
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] PreviewHandler::create multipartMap [-----[ ${multipartMap}" }
        val adminId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return previewService.create(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

    suspend fun findAll(request: ServerRequest): ServerResponse {
        val sort = Sort.by(Sort.Direction.DESC, "id")
        val page = if (request.queryParam("page").isPresent) request.queryParam("page").get().toInt() else 0
        val size = if (request.queryParam("size").isPresent) request.queryParam("size").get().toInt() else 10
        return previewService.findAll(PageRequest.of(page, size, sort), request.queryParams()).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        return previewService.findById(id).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] PreviewHandler::update multipartMap [-----[ ${multipartMap}" }
        val adminId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return previewService.update(id, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

    private fun mapToReq(multipartMap: MultiValueMap<String, Part>): CreatePreviewReq {
        log.debug { "]-----] PreviewHandler::create mapToReq [-----[ ${multipartMap}" }
        val partMap: Map<String, Part> = multipartMap.toSingleValueMap()

        var title: String = ""
        if (partMap.containsKey("name")) {
            title = (partMap["title"] as FormFieldPart).value()
            if (title.isBlank()) throw PreviewException(ErrorMessageCode.PREVIEW_TITLE_REQUIRED)
        } else {
            throw PreviewException(ErrorMessageCode.PREVIEW_TITLE_REQUIRED)
        }

        var description: String = ""
        if (partMap.containsKey("description")) {
            description = (partMap["description"] as FormFieldPart).value()
            if (description.isBlank()) throw PreviewException(ErrorMessageCode.PREVIEW_DESCRIPTION_REQUIRED)
        } else {
            throw PreviewException(ErrorMessageCode.PREVIEW_DESCRIPTION_REQUIRED)
        }

        var startAt: Instant
        if (partMap.containsKey("startAt")) {
            val startAtStr = (partMap["startAt"] as FormFieldPart).value()
            startAt = Instant.parse(startAtStr)
            if (startAtStr.isBlank()) throw BroadcastException(ErrorMessageCode.PREVIEW_STARTAT_REQUIRED)
        } else {
            throw BroadcastException(ErrorMessageCode.PREVIEW_STARTAT_REQUIRED)
        }

        var endAt: Instant
        if (partMap.containsKey("endAt")) {
            val endAtStr = (partMap["endAt"] as FormFieldPart).value()
            endAt = Instant.parse(endAtStr)
            if (endAtStr.isBlank()) throw BroadcastException(ErrorMessageCode.PREVIEW_ENDAT_REQUIRED)
        } else {
            throw BroadcastException(ErrorMessageCode.PREVIEW_ENDAT_REQUIRED)
        }

        var sortPosition: Int = 0
        if (partMap.containsKey("sortPosition")) {
            sortPosition = (partMap["sortPosition"] as FormFieldPart).value().toInt()
        } else {
            throw PreviewException(ErrorMessageCode.PREVIEW_SORTPOSITION_REQUIRED)
        }

        val updatedImages: MutableList<MediaReq> = mutableListOf()
        val newImages: MutableList<NewImage> = mutableListOf()

        if (partMap.containsKey("imageCount")) {
            val imageCount = (partMap["imageCount"] as FormFieldPart).value().toInt()
            if (imageCount < 0) throw PreviewException(ErrorMessageCode.PREVIEW_IMAGE_REQUIRED)
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
            throw PreviewException(ErrorMessageCode.PRODUCT_IMAGE_REQUIRED)
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
        return CreatePreviewReq(
            title = title,
            description = description,
            startAt = startAt,
            endAt = endAt,
            sortPosition = sortPosition,
            updatedImages = updatedImages,
            newImages = newImages,
            deletedImages = deletedImages

        )
    }
}
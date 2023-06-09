package com.clnk.livecommerce.api.admin.product

import com.clnk.livecommerce.api.library.exception.ErrorMessageCode
import com.clnk.livecommerce.api.library.exception.ProductException
import com.clnk.livecommerce.api.library.media.MediaReq
import com.clnk.livecommerce.api.library.media.NewImage
import com.clnk.livecommerce.api.library.product.CreateProductReq
import com.clnk.livecommerce.api.library.product.service.ProductService
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
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class ProductHandler(
    private val productService: ProductService,
    private val validator: Validator
) {

    suspend fun create(request: ServerRequest): ServerResponse {
        log.info { "]-----] ProductHandler::create call [-----[" }
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] ProductHandler::create multipartMap [-----[ ${multipartMap}" }
        val adminId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return productService.create(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

    suspend fun findAll(request: ServerRequest): ServerResponse {
        val sort = Sort.by(Sort.Direction.DESC, "id")
        val page = if (request.queryParam("page").isPresent) request.queryParam("page").get().toInt() else 0
        val size = if (request.queryParam("size").isPresent) request.queryParam("size").get().toInt() else 10
        return productService.findAll(PageRequest.of(page, size, sort), request.queryParams()).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        return productService.findById(id).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

    suspend fun update(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] ProductHandler::update multipartMap [-----[ ${multipartMap}" }
        val adminId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return productService.update(id, req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

    private fun mapToReq(multipartMap: MultiValueMap<String, Part>): CreateProductReq {
        log.debug { "]-----] ProductHandler::create mapToReq [-----[ ${multipartMap}" }
        val partMap: Map<String, Part> = multipartMap.toSingleValueMap()
        var brandId: Long = -1
        if (partMap.containsKey("brandId")) {
            brandId = (partMap["brandId"] as FormFieldPart).value().toLong()
        } else {
            throw ProductException(ErrorMessageCode.PRODUCT_BRAND_REQUIRED)
        }

        var name: String = ""
        if (partMap.containsKey("name")) {
            name = (partMap["name"] as FormFieldPart).value()
            if (name.isBlank()) throw ProductException(ErrorMessageCode.PRODUCT_NAME_REQUIRED)
        } else {
            throw ProductException(ErrorMessageCode.PRODUCT_NAME_REQUIRED)
        }

        var description: String = ""
        if (partMap.containsKey("description")) {
            description = (partMap["description"] as FormFieldPart).value()
            if (description.isBlank()) throw ProductException(ErrorMessageCode.PRODUCT_DESCRIPTION_REQUIRED)
        } else {
            throw ProductException(ErrorMessageCode.PRODUCT_DESCRIPTION_REQUIRED)
        }

        val updatedImages: MutableList<MediaReq> = mutableListOf()
        val newImages: MutableList<NewImage> = mutableListOf()

        if (partMap.containsKey("imageCount")) {
            val imageCount = (partMap["imageCount"] as FormFieldPart).value().toInt()
            if (imageCount < 0) throw ProductException(ErrorMessageCode.PRODUCT_IMAGE_REQUIRED)
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
            throw ProductException(ErrorMessageCode.PRODUCT_IMAGE_REQUIRED)
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
        return CreateProductReq(
            name = name,
            description = description,
            updatedImages = updatedImages,
            newImages = newImages,
            deletedImages = deletedImages,
            brandId = brandId
        )
    }
}
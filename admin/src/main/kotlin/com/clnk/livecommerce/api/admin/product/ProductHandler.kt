package com.clnk.livecommerce.api.admin.product

import com.clnk.livecommerce.api.exception.ErrorMessageCode
import com.clnk.livecommerce.api.exception.ProductException
import com.clnk.livecommerce.api.media.Media
import com.clnk.livecommerce.api.media.MediaReq
import com.clnk.livecommerce.api.product.CreateProductReq
import com.clnk.livecommerce.api.product.ProductMediaReq
import com.clnk.livecommerce.api.product.service.ProductService
import mu.KotlinLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.FormFieldPart
import org.springframework.http.codec.multipart.Part
import org.springframework.security.access.prepost.PreAuthorize
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

//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    suspend fun create(request: ServerRequest): ServerResponse {
        val multipartMap = request.awaitMultipartData()
        log.debug { "]-----] ProductHandler::create multipartMap [-----[ ${multipartMap}" }
        val adminId = request.awaitPrincipal()!!.name.toLong()
        val req = mapToReq(multipartMap)
        return productService.create(req, adminId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }

    }

    private fun mapToReq(multipartMap: MultiValueMap<String, Part>): CreateProductReq {
        log.debug { "]-----] ProductHandler::create mapToReq [-----[ ${multipartMap}" }
        val partMap: Map<String, Part> = multipartMap.toSingleValueMap()
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
        val newImages: MutableList<ProductMediaReq> = mutableListOf()
        val deletedImages: MutableList<Long> = mutableListOf()
        if (partMap.containsKey("imageCount")) {
            val imageCount = (partMap["imageCount"] as FormFieldPart).value().toInt()
            if(imageCount < 0) throw ProductException(ErrorMessageCode.PRODUCT_IMAGE_REQUIRED)
            for (i in 0 until imageCount) {
                val mediaId = (partMap["image[$i].id"] as FormFieldPart).value()
                val imagePart = partMap["image[$i]"]
                if (imagePart is FilePart) {
                    newImages.add(
                        ProductMediaReq(
                            productImage = imagePart,
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

        return CreateProductReq(
            name = name,
            description = description,
            updatedImages = updatedImages,
            newImages = newImages
        )
    }
}
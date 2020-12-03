package com.clnk.livecommerce.api.product.service.impl

import com.clnk.livecommerce.api.infra.MediaUtils
import com.clnk.livecommerce.api.media.Media
import com.clnk.livecommerce.api.media.repository.MediaRepository
import com.clnk.livecommerce.api.product.CreateProductReq
import com.clnk.livecommerce.api.product.CreateProductRes
import com.clnk.livecommerce.api.product.Product
import com.clnk.livecommerce.api.product.ProductRes
import com.clnk.livecommerce.api.product.repository.ProductRepository
import com.clnk.livecommerce.api.product.service.ProductService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
class ProductServiceImpl(

    private var productRepository: ProductRepository,
    private var mediaRepository: MediaRepository,
    private var modelMapper: ModelMapper,
    private var mediaUtils: MediaUtils,
) : ProductService {
    @Transactional
    override fun create(req: CreateProductReq, adminId: Long): CreateProductRes {
        log.info { "]-----] ProductServiceImpl::create CreateProductReq[-----[ ${req}" }
        val newProduct = Product(
            name = req.name,
            description = req.description
        )
        productRepository.save(newProduct)
        if (req.newImages.size > 0) {
            val medias: MutableList<Media> = mutableListOf()
            for (i in req.newImages.indices) {
                val mediaInfo = req.newImages[i].productImage?.let { mediaUtils.getMediaInfo(it, "product") }
                val newMedia = Media(
                    mediaUuid = newProduct.mediaUuid,
                    url = mediaInfo!!.fullPath,
                    originName = mediaInfo.originalName,
                    modifyName = mediaInfo.modifyName,
                    pathS3 = mediaInfo.bucketPath,
                    imageExt = mediaInfo.imageExt,
                    sortPosition = req.newImages[i].sortPosition
                )
                medias.add(newMedia)
            }
            mediaRepository.saveAll(medias)
        }

        return CreateProductRes(newProduct.id!!)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ProductRes> {
        val items = productRepository.findAllByActive(pageable, true)
        return items.map {
            modelMapper.map(it, ProductRes::class.java)
        }
    }
}
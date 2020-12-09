package com.clnk.livecommerce.api.product.service.impl

import com.clnk.livecommerce.api.brand.repository.BrandRepository
import com.clnk.livecommerce.api.infra.MediaUtils
import com.clnk.livecommerce.api.media.Media
import com.clnk.livecommerce.api.media.repository.MediaRepository
import com.clnk.livecommerce.api.product.*
import com.clnk.livecommerce.api.product.repository.ProductRepository
import com.clnk.livecommerce.api.product.service.ProductService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import javax.persistence.EntityNotFoundException

private val log = KotlinLogging.logger {}

@Service
class ProductServiceImpl(

    private var productRepository: ProductRepository,
    private var mediaRepository: MediaRepository,
    private var brandRepository: BrandRepository,
    private var modelMapper: ModelMapper,
    private var mediaUtils: MediaUtils,
) : ProductService {
    @Transactional
    override fun create(req: CreateProductReq, adminId: Long): CreateProductRes {
        log.info { "]-----] ProductServiceImpl::create CreateProductReq[-----[ ${req}" }
        val brand = brandRepository.findByIdAndActive(req.brandId, true)
            ?: throw EntityNotFoundException("not found a Brand(id = ${req.brandId})")
        val newProduct = Product(
            name = req.name,
            description = req.description,
            brand = brand
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
    override fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<ProductListRes> {
        val products = productRepository.findAllBySearch(pageable, queryParams)
        return products.map {
            modelMapper.map(it, ProductListRes::class.java)
        }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): ProductRes {
        val item = productRepository.findByIdAndActive(id, true)
        return modelMapper.map(item, ProductRes::class.java)
    }

    @Transactional
    override fun update(id: Long, req: CreateProductReq, adminId: Long): CreateProductRes {
        log.info { "]-----] ProductServiceImpl::update CreateProductReq[-----[ ${req}" }
        val product = productRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a Product(id = ${id})")
        product.name = req.name
        product.description = req.description
        productRepository.save(product)

        if (req.newImages.size > 0) {
            val medias: MutableList<Media> = mutableListOf()
            for (i in req.newImages.indices) {
                val mediaInfo = req.newImages[i].productImage?.let { mediaUtils.getMediaInfo(it, "product") }
                val newMedia = Media(
                    mediaUuid = product.mediaUuid,
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
        log.info { "]-----] ProductServiceImpl::update updatedImages[-----[ ${req.updatedImages.size}" }
        log.info { "]-----] ProductServiceImpl::update deletedImages[-----[ ${req.deletedImages.size}" }
        if (req.updatedImages.size > 0) {
            for (j in req.updatedImages.indices) {
                val media = mediaRepository.findByIdAndMediaUuidAndActive(req.updatedImages[j].id, product.mediaUuid, true)
                    ?: throw EntityNotFoundException("not found a Media(id = ${req.updatedImages[j].id})")
                media.sortPosition = req.updatedImages[j].sortPosition
                mediaRepository.save(media)
            }
        }
        if (req.deletedImages.size > 0) {
            mediaRepository.deleteByIds(req.deletedImages, product.mediaUuid)
        }
        return CreateProductRes(product.id!!)
    }
}
package com.clnk.livecommerce.api.product.service.impl

import com.clnk.livecommerce.api.infra.MediaUtils
import com.clnk.livecommerce.api.product.CreateProductReq
import com.clnk.livecommerce.api.product.CreateProductRes
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
    private var modelMapper: ModelMapper,
    private var mediaUtils: MediaUtils,
) : ProductService {
    @Transactional
    override fun create(req: CreateProductReq, adminId: Long): CreateProductRes {
        log.info { "]-----] ProductServiceImpl::create CreateProductReq[-----[ ${req}" }
        return CreateProductRes(-1)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ProductRes> {
        val items = productRepository.findAllByActive(pageable, true)
        return items.map {
            modelMapper.map(it, ProductRes::class.java)
        }
    }
}
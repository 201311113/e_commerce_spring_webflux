package com.clnk.livecommerce.api.product.service.impl

import com.clnk.livecommerce.api.infra.MediaUtils
import com.clnk.livecommerce.api.media.Media
import com.clnk.livecommerce.api.product.*
import com.clnk.livecommerce.api.product.repository.OptionGroupRepository
import com.clnk.livecommerce.api.product.repository.ProductRepository
import com.clnk.livecommerce.api.product.service.OptionService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

private val log = KotlinLogging.logger {}

@Service
class OptionServiceImpl(
    private var productRepository: ProductRepository,
    private var optionGroupRepository: OptionGroupRepository,
    private var modelMapper: ModelMapper,
    private var mediaUtils: MediaUtils,
) : OptionService {
    @Transactional
    override fun create(productId: Long, req: CreateOptionReq, adminId: Long): CreateOptionRes {
        log.info { "]-----] ProductServiceImpl::create CreateProductReq[-----[ ${req}" }
        val product = productRepository.findByIdAndActive(productId, true)
            ?: throw EntityNotFoundException("not found a Product(id = ${productId})")
        val newOptionGroup = modelMapper.map(req, OptionGroup::class.java)
        newOptionGroup.product = product
        newOptionGroup.createdId = adminId
        optionGroupRepository.save(newOptionGroup)

        return CreateOptionRes(newOptionGroup.id!!)
    }

    @Transactional(readOnly = true)
    override fun findAllByProductId(productId: Long): OptionGroupListRes {
        val items = optionGroupRepository.findAllByProductIdAndActive(productId, true)
        return OptionGroupListRes(
            content = items.map {
                modelMapper.map(it, OptionGroupRes::class.java)
            }.toMutableList()
        )
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): OptionGroupRes {
        val item = optionGroupRepository.findByIdAndActive(id, true)
        return modelMapper.map(item, OptionGroupRes::class.java)
    }

    @Transactional
    override fun update(id: Long, req: CreateOptionReq, adminId: Long): CreateOptionRes {

        return CreateOptionRes(-1)
    }

    @Transactional
    override fun updateOptionGroupSort(req: UpdateOptionGroupSortReq, adminId: Long): CreateOptionRes {
        return if (req.optionGroups.size > 0) {
            val optionGroups: MutableList<OptionGroup> = mutableListOf()
            for (i in req.optionGroups.indices) {
                val optionGroup = optionGroupRepository.findByIdAndActive(req.optionGroups[i].id, true)
                    ?: throw EntityNotFoundException("not found a OptionGroups(id = ${req.optionGroups[i].id})")
                optionGroup.sortPosition = req.optionGroups[i].sortPosition
                optionGroup.updatedId = adminId
                optionGroupRepository.save(optionGroup)
                optionGroups.add(optionGroup)
            }
            optionGroupRepository.saveAll(optionGroups)
            CreateOptionRes(1)
        } else {
            CreateOptionRes(-1)
        }

    }
}
package com.clnk.livecommerce.api.product.service.impl

import com.clnk.livecommerce.api.product.*
import com.clnk.livecommerce.api.product.repository.OptionGroupRepository
import com.clnk.livecommerce.api.product.repository.OptionItemRepository
import com.clnk.livecommerce.api.product.repository.ProductRepository
import com.clnk.livecommerce.api.product.service.OptionGroupService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

private val log = KotlinLogging.logger {}

@Service
class OptionGroupServiceImpl(
    private var productRepository: ProductRepository,
    private var optionGroupRepository: OptionGroupRepository,
    private var optionItemRepository: OptionItemRepository,
    private var modelMapper: ModelMapper
) : OptionGroupService {
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
        val optionGroups = optionGroupRepository.findAllByProductIdAndActiveOrderBySortPositionAscIdDesc(productId, true)
        return OptionGroupListRes(
            content = optionGroups.map {
                modelMapper.map(it, OptionGroupRes::class.java)
            }.toMutableList()
        )
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): OptionGroupRes {
        val optionGroup = optionGroupRepository.findByIdAndActive(id, true)
        return modelMapper.map(optionGroup, OptionGroupRes::class.java)
    }

    @Transactional
    override fun update(id: Long, req: UpdateOptionReq, adminId: Long): CreateOptionRes {
        val optionGroup = optionGroupRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a OptionGroups(id = ${id})")
        optionGroup.title = req.title
        optionGroup.updatedId = adminId
        optionGroupRepository.save(optionGroup)

        if (req.newOptionItems.size > 0) {
            val newOptionItems: MutableList<OptionItem> = mutableListOf()
            for (i in req.newOptionItems.indices) {
                val newOptionItemReq = req.newOptionItems[i]
                val newOptionItem = OptionItem(
                    name = newOptionItemReq.name,
                    sortPosition = newOptionItemReq.sortPosition,
                    optionGroup = optionGroup
                )
                newOptionItems.add(newOptionItem)
            }
            optionItemRepository.saveAll(newOptionItems)
        }

        if (req.updatedOptionItems.size > 0) {
            for (j in req.updatedOptionItems.indices) {
                val optionItemReq = req.updatedOptionItems[j]
                val optionItem = optionItemRepository.findByIdAndActive(optionItemReq.id, true)
                    ?: throw EntityNotFoundException("not found a OptionItem(id = ${optionItemReq.id})")
                optionItem.name = optionItemReq.name
                optionItem.sortPosition = optionItemReq.sortPosition
                optionItem.updatedId = adminId
                optionItemRepository.save(optionItem)
            }
        }
        if (req.deletedOptionItemIds.size > 0) {
            optionItemRepository.deleteByIds(req.deletedOptionItemIds, id)
        }
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

    @Transactional
    override fun delete(id: Long, adminId: Long): CreateOptionRes {
        val optionGroup = optionGroupRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a OptionGroups(id = ${id})")
        optionGroup.active = false
        optionGroupRepository.save(optionGroup)
        return CreateOptionRes(-1)
    }

}
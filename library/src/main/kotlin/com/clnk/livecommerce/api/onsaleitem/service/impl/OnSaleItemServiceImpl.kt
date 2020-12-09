package com.clnk.livecommerce.api.onsaleitem.service.impl

import com.clnk.livecommerce.api.onsaleitem.*
import com.clnk.livecommerce.api.onsaleitem.repository.OnSaleItemOptionGroupRepository
import com.clnk.livecommerce.api.onsaleitem.repository.OnSaleItemOptionRepository
import com.clnk.livecommerce.api.onsaleitem.repository.OnSaleItemRepository
import com.clnk.livecommerce.api.onsaleitem.service.OnSaleItemService
import com.clnk.livecommerce.api.product.repository.OptionGroupRepository
import com.clnk.livecommerce.api.product.repository.OptionItemRepository
import com.clnk.livecommerce.api.product.repository.ProductRepository
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
class OnSaleItemServiceImpl(
    private var productRepository: ProductRepository,
    private var onSaleItemRepository: OnSaleItemRepository,
    private var optionGroupRepository: OptionGroupRepository,
    private var optionItemRepository: OptionItemRepository,
    private var onSaleItemOptionGroupRepository: OnSaleItemOptionGroupRepository,
    private var onSaleItemOptionRepository: OnSaleItemOptionRepository,
    private var modelMapper: ModelMapper
) : OnSaleItemService {
    @Transactional
    override fun create(req: CreateOnSaleItemReq, adminId: Long): CreateOnSaleItemRes {
        log.info { "]-----] OnSaleItemServiceImpl::create CreateProductReq[-----[ ${req}" }
        val productId = req.productId
        val product = productRepository.findByIdAndActive(productId, true)
            ?: throw EntityNotFoundException("not found a Product(id = ${productId})")
        val newOnSaleItem = OnSaleItem(
            title = req.title,
            sellPrice = req.sellPrice,
            stock = req.stock,
            description = req.description,
            hashTags = req.hashTags,
            product = product,
            deliveryPrice = req.deliveryPrice,
            isGroupdelivery = req.isGroupdelivery
        )
        onSaleItemRepository.save(newOnSaleItem)

        if (req.onSaleItemOptionGroups.size > 0) {
            for (i in req.onSaleItemOptionGroups.indices) {
                val onSaleItemOptionGroupReq = req.onSaleItemOptionGroups[i]
                val optionGroup = optionGroupRepository.findByIdAndProductIdAndActive(onSaleItemOptionGroupReq.optionGroupId, productId, true)
                    ?: throw EntityNotFoundException("not found a OptionGroup(id = ${onSaleItemOptionGroupReq.optionGroupId})")
                val newOnSaleItemOptionGroup = OnSaleItemOptionGroup(
                    onSaleItem = newOnSaleItem,
                    optionGroup = optionGroup

                )
                onSaleItemOptionGroupRepository.save(newOnSaleItemOptionGroup)
                val newOnSaleItemOptions = onSaleItemOptionGroupReq.onSaleItemOptions.map {
                    val optionItem = optionGroup.id?.let { it1 -> optionItemRepository.findByIdAndOptionGroupIdAndActive(it.optionItemId, it1, true) }
                        ?: throw EntityNotFoundException("not found a OptionItem(id = ${it.optionItemId})")
                    OnSaleItemOption(
                        optionPrice = it.optionPrice,
                        stock = it.stock,
                        optionItem = optionItem,
                        onSaleItemOptionGroup = newOnSaleItemOptionGroup
                    )
                }.toMutableList()
                onSaleItemOptionRepository.saveAll(newOnSaleItemOptions)
            }
        }
        return CreateOnSaleItemRes(newOnSaleItem.id!!)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long, adminId: Long): OnSaleItemRes {
        val onSaleItem = onSaleItemRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a OnSaleItem(id = ${id})")
        return modelMapper.map(onSaleItem, OnSaleItemRes::class.java)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<OnSaleItemResForList> {
        val onSaleItems = onSaleItemRepository.findAllBySearch(pageable, queryParams)
        return onSaleItems.map {
            modelMapper.map(it, OnSaleItemResForList::class.java)
        }
    }

    @Transactional
    override fun update(id: Long, req: CreateOnSaleItemReq, adminId: Long): CreateOnSaleItemRes {
        log.info { "]-----] OnSaleItemServiceImpl::update CreateProductReq[-----[ ${req}" }
        val onSaleItem = onSaleItemRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a OnSaleItem(id = ${id})")

        onSaleItem.title = req.title
        onSaleItem.sellPrice = req.sellPrice
        onSaleItem.stock = req.stock
        onSaleItem.description = req.description
        onSaleItem.hashTags = req.hashTags
        onSaleItemRepository.save(onSaleItem)

        if (req.onSaleItemOptionGroups.size > 0) {
            for (i in req.onSaleItemOptionGroups.indices) {
                val onSaleItemOptionGroupReq = req.onSaleItemOptionGroups[i]
                val onSaleItemOptions = onSaleItemOptionGroupReq.onSaleItemOptions.map {
                    val onSaleItemOption = onSaleItemOptionRepository.findByIdAndOnSaleItemOptionGroupIdAndActive(it.id, onSaleItemOptionGroupReq.id, true)
                        ?: throw EntityNotFoundException("not found a OnSaleItemOption(id = ${it.id})")
                    onSaleItemOption.optionPrice = it.optionPrice
                    onSaleItemOption.stock = it.stock
                    onSaleItemOption
                }.toMutableList()
                onSaleItemOptionRepository.saveAll(onSaleItemOptions)
            }
        }
        return CreateOnSaleItemRes(onSaleItem.id!!)
    }
}
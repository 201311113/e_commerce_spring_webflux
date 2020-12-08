package com.clnk.livecommerce.api.onsaleitem.service.impl

import com.clnk.livecommerce.api.onsaleitem.*
import com.clnk.livecommerce.api.onsaleitem.repository.OnSaleItemOptionGroupRepository
import com.clnk.livecommerce.api.onsaleitem.repository.OnSaleItemOptionRepository
import com.clnk.livecommerce.api.onsaleitem.repository.OnSaleItemRepository
import com.clnk.livecommerce.api.onsaleitem.service.OnSaleItemService
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
        log.info { "]-----] ProductServiceImpl::create CreateProductReq[-----[ ${req}" }
        val productId = req.productId
        val product = productRepository.findByIdAndActive(productId, true)
            ?: throw EntityNotFoundException("not found a Product(id = ${productId})")
        val newOnSaleItem = OnSaleItem(
            title = req.title,
            sellPrice = req.sellPrice,
            stock = req.stock,
            description = req.description,
            hashTags = req.hashTags,
            product = product
        )
        onSaleItemRepository.save(newOnSaleItem)

        if (req.optionGroups.size > 0) {
            for (i in req.optionGroups.indices) {
                val onSaleItemOptionGroupReq = req.optionGroups[i]
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


}
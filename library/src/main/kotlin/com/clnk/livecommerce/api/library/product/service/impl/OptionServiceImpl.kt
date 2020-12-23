package com.clnk.livecommerce.api.library.product.service.impl

import com.clnk.livecommerce.api.library.product.OptionItemRes
import com.clnk.livecommerce.api.library.product.repository.OptionItemRepository
import com.clnk.livecommerce.api.library.product.service.OptionService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
class OptionServiceImpl(
    private var optionItemRepository: OptionItemRepository,
    private var modelMapper: ModelMapper

) : OptionService {
    @Transactional(readOnly = true)
    override fun findById(id: Long): OptionItemRes {
        val optionItem = optionItemRepository.findByIdAndActive(id, true)
        return modelMapper.map(optionItem, OptionItemRes::class.java)
    }

}
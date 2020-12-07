package com.clnk.livecommerce.api.product.service.impl

import com.clnk.livecommerce.api.product.OptionItemRes
import com.clnk.livecommerce.api.product.repository.OptionItemRepository
import com.clnk.livecommerce.api.product.service.OptionService
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
        val item = optionItemRepository.findByIdAndActive(id, true)
        return modelMapper.map(item, OptionItemRes::class.java)
    }

}
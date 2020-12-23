package com.clnk.livecommerce.api.library.brand.service.impl

import com.clnk.livecommerce.api.library.brand.Brand
import com.clnk.livecommerce.api.library.brand.BrandRes
import com.clnk.livecommerce.api.library.brand.CreateBrandReq
import com.clnk.livecommerce.api.library.brand.CreateBrandRes
import com.clnk.livecommerce.api.library.brand.repository.BrandRepository
import com.clnk.livecommerce.api.library.brand.service.BrandService
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
class BrandServiceImpl(
    private var brandRepository: BrandRepository,
    private var modelMapper: ModelMapper
) : BrandService {
    @Transactional
    override fun create(req: CreateBrandReq, adminId: Long): CreateBrandRes {
        log.info { "]-----] BrandServiceImpl::create CreateBrandReq[-----[ ${req}" }
        val newBrand = Brand(
            name = req.name,
            managerName = req.managerName,
            managerPhoneNumber = req.managerPhoneNumber
        )
        brandRepository.save(newBrand)
        return CreateBrandRes(newBrand.id!!)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long, adminId: Long): BrandRes {
        val brand = brandRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a Brand(id = ${id})")
        return modelMapper.map(brand, BrandRes::class.java)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<BrandRes> {
        val brands = brandRepository.findAllBySearch(pageable, queryParams)
        return brands.map {
            modelMapper.map(it, BrandRes::class.java)
        }
    }

    @Transactional
    override fun update(id: Long, req: CreateBrandReq, adminId: Long): CreateBrandRes {
        log.info { "]-----] OnSaleItemServiceImpl::update CreateProductReq[-----[ ${req}" }
        val brand = brandRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a Brand(id = ${id})")

        brand.name = req.name
        brand.managerName = req.managerName
        brand.managerPhoneNumber = req.managerPhoneNumber
        brandRepository.save(brand)
        return CreateBrandRes(brand.id!!)
    }
}
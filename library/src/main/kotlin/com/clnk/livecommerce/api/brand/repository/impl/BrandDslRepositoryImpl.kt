package com.clnk.livecommerce.api.brand.repository.impl

import com.clnk.livecommerce.api.brand.Brand
import com.clnk.livecommerce.api.brand.BrandSearchCondition
import com.clnk.livecommerce.api.brand.QBrand.brand
import com.clnk.livecommerce.api.brand.repository.BrandDslRepository
import com.clnk.livecommerce.api.support.QuerydslCustomRepositorySupport
import com.querydsl.core.BooleanBuilder
import mu.KotlinLogging
import org.apache.commons.lang3.EnumUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.util.MultiValueMap

private val logger = KotlinLogging.logger {}

@Repository
class BrandDslRepositoryImpl : QuerydslCustomRepositorySupport(Brand::class.java), BrandDslRepository {
    override fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Brand> {
        val whereClause = BooleanBuilder()
        if (queryParams.size > 0) {
            for (key in queryParams.keys) {
                if (EnumUtils.isValidEnum(BrandSearchCondition::class.java, key.toUpperCase())) {
                    val searchContent = queryParams[key]!![0]
                    if (StringUtils.isNoneBlank(searchContent)) {
                        when (key.toLowerCase()) {
                            BrandSearchCondition.NAME.searchKey -> whereClause.apply { this.and(brand.name.like("%${searchContent}%")) }
                            BrandSearchCondition.MANAGERNAME.searchKey -> whereClause.apply { this.and(brand.managerName.like("%${searchContent}%")) }
                            BrandSearchCondition.MANAGERPHONENUMBER.searchKey -> whereClause.apply { this.and(brand.managerPhoneNumber.like("%${searchContent}%")) }
                        }
                    }
                }
            }
        }
        val query = select(brand).from(brand).where(brand.active.eq(true).and(whereClause))
        val products: List<Brand> = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(products, pageable, query.fetchCount())
    }

}

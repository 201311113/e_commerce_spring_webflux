package com.clnk.livecommerce.api.product.repository.impl

import com.clnk.livecommerce.api.product.Product
import com.clnk.livecommerce.api.product.ProductSearchCondition
import com.clnk.livecommerce.api.product.QProduct.product
import com.clnk.livecommerce.api.product.repository.ProductDslRepository
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
class ProductDslRepositoryImpl : QuerydslCustomRepositorySupport(Product::class.java), ProductDslRepository {
    override fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Product> {
        val whereClause = BooleanBuilder()
        if (queryParams.size > 0) {
            for (key in queryParams.keys) {
                if (EnumUtils.isValidEnum(ProductSearchCondition::class.java, key.toUpperCase())) {
                    val searchContent = queryParams[key]!![0]
                    if (StringUtils.isNoneBlank(searchContent)) {
                        when (key.toLowerCase()) {
                            ProductSearchCondition.NAME.searchKey -> whereClause.apply { this.and(product.name.like("%${searchContent}%")) }
                            ProductSearchCondition.DESCRIPTION.searchKey -> whereClause.apply { this.and(product.description.like("%${searchContent}%")) }
                        }
                    }
                }
            }
        }
        val query = select(product).from(product).where(product.active.eq(true).and(whereClause))
        val products: List<Product> = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(products, pageable, query.fetchCount())
    }

}

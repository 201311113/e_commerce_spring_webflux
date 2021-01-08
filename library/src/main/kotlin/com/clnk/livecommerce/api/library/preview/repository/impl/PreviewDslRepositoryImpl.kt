package com.clnk.livecommerce.api.library.onsaleitem.repository.impl

import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItem
import com.clnk.livecommerce.api.library.onsaleitem.OnSaleItemSearchCondition
import com.clnk.livecommerce.api.library.onsaleitem.repository.OnSaleItemDslRepository
import com.clnk.livecommerce.api.library.support.QuerydslCustomRepositorySupport
import com.clnk.livecommerce.api.library.onsaleitem.QOnSaleItem.onSaleItem
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
class OnSaleItemDslRepositoryImpl : QuerydslCustomRepositorySupport(OnSaleItem::class.java), OnSaleItemDslRepository {
    override fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<OnSaleItem> {
        val whereClause = BooleanBuilder()
        if (queryParams.size > 0) {
            for (key in queryParams.keys) {
                if (EnumUtils.isValidEnum(OnSaleItemSearchCondition::class.java, key.toUpperCase())) {
                    val searchContent = queryParams[key]!![0]
                    if (StringUtils.isNoneBlank(searchContent)) {
                        when (key.toLowerCase()) {
                            OnSaleItemSearchCondition.TITLE.searchKey -> whereClause.apply { this.and(onSaleItem.title.like("%${searchContent}%")) }
                            OnSaleItemSearchCondition.DESCRIPTION.searchKey -> whereClause.apply { this.and(onSaleItem.description.like("%${searchContent}%")) }
                            OnSaleItemSearchCondition.PRODUCTNAME.searchKey -> whereClause.apply { this.and(onSaleItem.product.name.like("%${searchContent}%")) }
                        }
                    }
                }
            }
        }
        val query = select(onSaleItem).from(onSaleItem).where(onSaleItem.active.eq(true).and(whereClause))
        val onSaleItems: List<OnSaleItem> = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(onSaleItems, pageable, query.fetchCount())
    }

}

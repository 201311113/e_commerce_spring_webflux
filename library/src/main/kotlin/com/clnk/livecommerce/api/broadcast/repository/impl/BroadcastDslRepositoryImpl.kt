package com.clnk.livecommerce.api.broadcast.repository.impl

import com.clnk.livecommerce.api.broadcast.Broadcast
import com.clnk.livecommerce.api.broadcast.BroadcastSearchCondition
import com.clnk.livecommerce.api.broadcast.QBroadcast.broadcast
import com.clnk.livecommerce.api.broadcast.repository.BroadcastDslRepository
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
class BroadcastDslRepositoryImpl : QuerydslCustomRepositorySupport(Broadcast::class.java), BroadcastDslRepository {
    override fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Broadcast> {
        val whereClause = BooleanBuilder()
        if (queryParams.size > 0) {
            for (key in queryParams.keys) {
                if (EnumUtils.isValidEnum(BroadcastSearchCondition::class.java, key.toUpperCase())) {
                    val searchContent = queryParams[key]!![0]
                    if (StringUtils.isNoneBlank(searchContent)) {
                        when (key.toLowerCase()) {
                            BroadcastSearchCondition.TITLE.searchKey -> whereClause.apply { this.and(broadcast.title.like("%${searchContent}%")) }
                            BroadcastSearchCondition.DESCRIPTION.searchKey -> whereClause.apply { this.and(broadcast.description.like("%${searchContent}%")) }
                        }
                    }
                }
            }
        }
        val query = select(broadcast).from(broadcast).where(broadcast.active.eq(true).and(whereClause))
        val products: List<Broadcast> = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(products, pageable, query.fetchCount())
    }

}

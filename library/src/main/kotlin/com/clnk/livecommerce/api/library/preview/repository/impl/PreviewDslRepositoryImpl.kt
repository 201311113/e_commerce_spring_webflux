package com.clnk.livecommerce.api.library.preview.repository.impl

import com.clnk.livecommerce.api.library.preview.Preview
import com.clnk.livecommerce.api.library.preview.PreviewSearchCondition
import com.clnk.livecommerce.api.library.preview.QPreview.preview
import com.clnk.livecommerce.api.library.preview.repository.PreviewDslRepository
import com.clnk.livecommerce.api.library.support.QuerydslCustomRepositorySupport
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
class PreviewDslRepositoryImpl : QuerydslCustomRepositorySupport(Preview::class.java), PreviewDslRepository {
    override fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<Preview> {
        val whereClause = BooleanBuilder()
        if (queryParams.size > 0) {
            for (key in queryParams.keys) {
                if (EnumUtils.isValidEnum(PreviewSearchCondition::class.java, key.toUpperCase())) {
                    val searchContent = queryParams[key]!![0]
                    if (StringUtils.isNoneBlank(searchContent)) {
                        when (key.toLowerCase()) {
                            PreviewSearchCondition.TITLE.searchKey -> whereClause.apply { this.and(preview.title.like("%${searchContent}%")) }
                            PreviewSearchCondition.DESCRIPTION.searchKey -> whereClause.apply { this.and(preview.description.like("%${searchContent}%")) }

                        }
                    }
                }
            }
        }
        val query = select(preview).from(preview).where(preview.active.eq(true).and(whereClause))
        val previews: List<Preview> = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(previews, pageable, query.fetchCount())
    }

}

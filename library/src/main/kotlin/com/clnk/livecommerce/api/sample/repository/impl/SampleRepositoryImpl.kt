package com.clnk.livecommerce.api.sample.repository.impl

import com.clnk.livecommerce.api.sample.QSample.sample
import com.clnk.livecommerce.api.sample.Sample
import com.clnk.livecommerce.api.sample.repository.SampleDslRepository
import com.clnk.livecommerce.api.support.QuerydslCustomRepositorySupport
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

private val log = KotlinLogging.logger {}

class SampleRepositoryImpl : QuerydslCustomRepositorySupport(Sample::class.java), SampleDslRepository {
    override fun findAllByPage(pageable: Pageable): Page<Sample> {
        val query = select(sample)
            .from(sample)
            .where(sample.active.eq(true))
        val products: List<Sample> = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(products, pageable, query.fetchCount())
    }
}
package com.clnk.livecommerce.api.sample.repository

import com.clnk.livecommerce.api.sample.Sample
import org.springframework.data.jpa.repository.JpaRepository

interface SampleRepository : JpaRepository<Sample, Long>, SampleDslRepository {
    fun findByTitleAndActive(title: String, active: Boolean): Sample?
    fun findByIdAndActive(id: Long, active: Boolean): Sample?
}
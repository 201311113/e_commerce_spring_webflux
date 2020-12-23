package com.clnk.livecommerce.api.library.sample.repository

import com.clnk.livecommerce.api.library.sample.Sample
import org.springframework.data.jpa.repository.JpaRepository

interface SampleRepository : JpaRepository<Sample, Long>, SampleDslRepository {
    fun findByTitleAndActive(title: String, active: Boolean): Sample?
    fun findByIdAndActive(id: Long, active: Boolean): Sample?
}
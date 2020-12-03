package com.clnk.livecommerce.api.media.repository

import com.clnk.livecommerce.api.media.Media
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

interface MediaRepository : JpaRepository<Media, Long> {
    fun findByIdAndActive(id: Long, active: Boolean): Media?
    fun findByIdAndMediaUuidAndActive(id: Long, mediaUuid: String, active: Boolean): Media?

    @Transactional
    @Modifying
    @Query(value = "delete from media where id in (:deletes) and media_uuid = :mediaUuid", nativeQuery = true)
    fun deleteByIds(@Param("deletes") deletes: List<Long>, @Param("mediaUuid") mediaUuid: String)
}
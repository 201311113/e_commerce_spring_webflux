package com.clnk.livecommerce.api.media.repository

import com.clnk.livecommerce.api.media.Media
import org.springframework.data.jpa.repository.JpaRepository

interface MediaRepository : JpaRepository<Media, Long>
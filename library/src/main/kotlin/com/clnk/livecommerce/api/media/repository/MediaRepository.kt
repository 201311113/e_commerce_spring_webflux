package com.cucurbita.api.media.repository

import com.cucurbita.api.media.Media
import org.springframework.data.jpa.repository.JpaRepository

interface MediaRepository : JpaRepository<Media, Long>
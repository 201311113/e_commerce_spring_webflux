package com.clnk.livecommerce.api.application.auth.service

import com.clnk.livecommerce.api.library.member.SnsType
import com.clnk.livecommerce.api.application.auth.service.impl.TokenRes


interface AuthService {
    fun getAccessToken(memberId: Long): TokenRes
    fun verifyAccessToken(accessToken: String, snsType: SnsType, snsId: String): Boolean
}
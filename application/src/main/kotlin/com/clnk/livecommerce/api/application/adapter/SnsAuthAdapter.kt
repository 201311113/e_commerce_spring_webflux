package com.clnk.livecommerce.api.application.adapter

interface SnsAuthAdapter {
    fun verifyAccessToken(accessToken: String, snsId: String): Boolean = false
}
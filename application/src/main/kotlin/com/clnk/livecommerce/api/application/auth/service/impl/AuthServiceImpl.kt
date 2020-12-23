package com.clnk.livecommerce.api.application.auth.service.impl

import com.clnk.livecommerce.api.application.adapter.AppleAuth
import com.clnk.livecommerce.api.application.adapter.FacebookAuth
import com.clnk.livecommerce.api.application.adapter.GoogleAuth
import com.clnk.livecommerce.api.application.auth.service.AuthService
import com.clnk.livecommerce.api.application.common.config.security.JWTService
import com.clnk.livecommerce.api.application.common.exception.ApplicationErrorMessageCode
import com.clnk.livecommerce.api.application.common.exception.SigninSnsException
import com.clnk.livecommerce.api.library.member.SnsType
import com.clnk.livecommerce.api.library.member.repository.MemberRepository
import com.clnk.livecommerce.api.application.adapter.KakaoAuth
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

private val logger = KotlinLogging.logger {}

@Service
class AuthServiceImpl(
    val jwtService: JWTService,
    val memberRepository: MemberRepository,
    val facebookAuth: FacebookAuth,
    val kakaoAuth: KakaoAuth,
    val googleAuth: GoogleAuth,
    val appleAuth: AppleAuth
) : AuthService {
    @Transactional(readOnly = true)
    override fun getAccessToken(memberId: Long): TokenRes {
        val member = memberRepository.findByIdAndActive(memberId, true)
            ?: throw EntityNotFoundException("not found a Member(id = ${memberId})")
        val authorities = member.roles.map { memberRole -> memberRole.role.roleType.name }.toTypedArray()
        val token: String = jwtService.accessToken(member.id.toString(), authorities)
        return TokenRes(token, member.snsId)
    }

    override fun verifyAccessToken(accessToken: String, snsType: SnsType, snsId: String): Boolean =
        when (snsType) {
            SnsType.EMAIL -> true
            SnsType.FACEBOOK -> facebookAuth.verifyAccessToken(accessToken, snsId)
            SnsType.KAKAO -> kakaoAuth.verifyAccessToken(accessToken, snsId)
            SnsType.GOOGLE -> googleAuth.verifyAccessToken(accessToken, snsId)
            SnsType.APPLE -> appleAuth.verifyAccessToken(accessToken, snsId)
            else -> throw SigninSnsException(ApplicationErrorMessageCode.SNSTYPE_IS_MISMATCHED)
        }
}

data class TokenRes(
    val accessToken: String,
    val username: String,
    var certCount: Int? =null
)

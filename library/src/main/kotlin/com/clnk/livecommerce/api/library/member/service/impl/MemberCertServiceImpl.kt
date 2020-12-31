package com.clnk.livecommerce.api.library.member.service.impl

import com.clnk.livecommerce.api.library.infra.KakaoAlimSender
import com.clnk.livecommerce.api.library.member.*
import com.clnk.livecommerce.api.library.member.repository.MemberCertRepository
import com.clnk.livecommerce.api.library.member.repository.MemberRepository
import com.clnk.livecommerce.api.library.member.service.MemberCertService
import mu.KotlinLogging
import org.apache.commons.lang3.RandomUtils
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import javax.persistence.EntityNotFoundException

private val log = KotlinLogging.logger {}

@Service
class MemberCertServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberCertRepository: MemberCertRepository,
    private val kakaoAlimSender: KakaoAlimSender,
    private var modelMapper: ModelMapper
) : MemberCertService {
    @Transactional
    override fun create(req: SendVerifyCodeReq): SendVerifyCodeRes {
        log.debug { "]-----] MemberCertServiceImpl::create sendVerifyCodeReq[-----[ ${req}" }
        val newMemberCert = MemberCert(
            certPhoneNumber = req.phoneNumber,
            certNumber = RandomUtils.nextInt(100000, 999999).toString(),
            requestedAt = Instant.now()
        )
        memberCertRepository.save(newMemberCert)
        kakaoAlimSender.sendPhoneNumberVerifyCode(newMemberCert.certPhoneNumber, newMemberCert.certNumber)
        return SendVerifyCodeRes(
            id = newMemberCert.id!!
        )
    }

    @Transactional
    override fun verifyCode(req: PhoneNumberVerifyReq): PhoneNumberVerifyRes {
        log.debug { "]-----] MemberCertServiceImpl::verifyCode phoneNumberVerifyReq[-----[ ${req}" }
        val memberCert = memberCertRepository.findFirstByCertPhoneNumberAndActiveOrderByIdDesc(req.phoneNumber, true)
            ?: throw EntityNotFoundException("not found a memberCert(phoneNumber = ${req.phoneNumber})")
        return if (memberCert.certNumber == req.verifyCode) {
            // TODO: expired check
            PhoneNumberVerifyRes(isVerified = true)
        } else {
            PhoneNumberVerifyRes(isVerified = false)
        }
    }

}
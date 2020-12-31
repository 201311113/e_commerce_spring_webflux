package com.clnk.livecommerce.api.library.member.service.impl

import com.clnk.livecommerce.api.library.exception.ErrorMessageCode
import com.clnk.livecommerce.api.library.exception.SignupException
import com.clnk.livecommerce.api.library.member.*
import com.clnk.livecommerce.api.library.member.repository.MemberRepository
import com.clnk.livecommerce.api.library.member.repository.MemberRoleRepository
import com.clnk.livecommerce.api.library.member.repository.RoleRepository
import com.clnk.livecommerce.api.library.member.service.MemberService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import javax.persistence.EntityNotFoundException
import kotlin.experimental.and

private val log = KotlinLogging.logger {}
const val USERNAME_LENGTH = 12
private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val roleRepository: RoleRepository,
    private var modelMapper: ModelMapper
) : MemberService {
    @Transactional
    override fun signup(req: SignupReq): SignupRes {
        log.debug { "]-----] MemberServiceImpl::signup SignupReq[-----[ ${req}" }
        val exists = memberRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, SnsType.EMAIL, true)
        if (exists != null) {
            throw SignupException(ErrorMessageCode.SNSID_ALREADY_EXISTS)
        }
        return SignupRes(createMember(req.snsId, SnsType.EMAIL, null, req.password).id!!)
    }

    @Transactional
    override fun signupFirebaseEmail(req: SignupFirebaseEmailReq): SignupRes {
        log.debug { "]-----] MemberServiceImpl::signupFirebaseEmail SignupFirebaseEmailReq[-----[ ${req}" }
        val exists = memberRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, SnsType.EMAIL, true)
        if (exists != null) {
            throw SignupException(ErrorMessageCode.SNSID_ALREADY_EXISTS)
        }
        return SignupRes(createMember(req.snsId, SnsType.EMAIL, null, null).id!!)
    }

    @Transactional
    override fun signinSns(req: SigninSnsReq): SignupRes {
        return memberRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, req.snsType, true)?.let { SignupRes(it.id!!) }
            ?: SignupRes(createMember(req.snsId, req.snsType, req.snsToken, null).id!!)
    }

    @Transactional(readOnly = true)
    override fun duplicateCheckBySnsId(duplicateCheckReq: DuplicateCheckReq): DuplicateCheckRes {
        log.debug { "]-----] MemberServiceImpl::duplicateCheckBySnsId duplicateCheckReq[-----[ ${duplicateCheckReq}" }
        memberRepository.findBySnsIdAndSnsTypeAndActive(duplicateCheckReq.snsId, SnsType.EMAIL, true)
            ?: return DuplicateCheckRes(isDuplicated = false)
        return DuplicateCheckRes(
            isDuplicated = true
        )
    }

    @Transactional(readOnly = true)
    override fun duplicateCheckByNickName(duplicateCheckNickNameReq: DuplicateCheckNickNameReq): DuplicateCheckRes {
        log.debug { "]-----] MemberServiceImpl::duplicateCheckByNickName duplicateCheckReq[-----[ ${duplicateCheckNickNameReq}" }
        memberRepository.findByNickNameAndActive(duplicateCheckNickNameReq.nickName, true)
            ?: return DuplicateCheckRes(isDuplicated = false)
        return DuplicateCheckRes(
            isDuplicated = true
        )
    }

    @Transactional(readOnly = true)
    override fun findById(memberId: Long): MemberRes {
        return memberRepository.findByIdAndActive(memberId, true)?.let { modelMapper.map(it, MemberRes::class.java) }
            ?: throw EntityNotFoundException("not found a Member(id = ${memberId})")
    }

    private fun createMember(snsId: String, snsType: SnsType, snsToken: String?, password: String?): Member {
        val userRole = roleRepository.findByRoleType(RoleType.ROLE_USER)
            ?: throw EntityNotFoundException("not found a Role(type = ${RoleType.ROLE_USER.name})")
        return memberRepository.save(
            Member(
                snsId = snsId,
                snsType = snsType,
                nickName = makeRandomUsername(),
                snsToken = snsToken,
                password = password
            )
        )
            .also {
                val memberRole = memberRoleRepository.save(MemberRole(it, userRole))
                it.addRole(memberRole)
                log.debug { "]-----] MemberServiceImpl::createMember newMember[-----[ $it" }
            }

    }

    private fun makeRandomUsername(): String {
        val bytes = ByteArray(USERNAME_LENGTH)
        SecureRandom().nextBytes(bytes)

        return (bytes.indices).map { i ->
            charPool[(bytes[i] and -1 and 61).toInt()]
        }.joinToString("")
    }
}
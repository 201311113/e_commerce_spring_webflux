package com.clnk.livecommerce.api.library.member.service.impl

import com.clnk.livecommerce.api.library.exception.ErrorMessageCode
import com.clnk.livecommerce.api.library.exception.SignupException
import com.clnk.livecommerce.api.library.member.*
import com.clnk.livecommerce.api.library.member.repository.MemberInfoRepository
import com.clnk.livecommerce.api.library.member.repository.MemberRoleRepository
import com.clnk.livecommerce.api.library.member.repository.RoleRepository
import com.clnk.livecommerce.api.library.member.service.MemberService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import java.security.SecureRandom
import javax.persistence.EntityNotFoundException
import kotlin.experimental.and

private val log = KotlinLogging.logger {}
const val USERNAME_LENGTH = 12
private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

@Service
class MemberServiceImpl(
    private val memberInfoRepository: MemberInfoRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val roleRepository: RoleRepository,
    private var modelMapper: ModelMapper
) : MemberService {
    @Transactional
    override fun signup(req: SignupReq): SignupRes {
        log.debug { "]-----] MemberServiceImpl::signup SignupReq[-----[ ${req}" }
        val exists = memberInfoRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, SnsType.EMAIL, true)
        if (exists != null) {
            throw SignupException(ErrorMessageCode.SNSID_ALREADY_EXISTS)
        }
        val newMember = MemberInfo(
            snsId = req.snsId,
            snsType = SnsType.EMAIL,
            nickName = makeRandomUsername(),
            password = req.password
        )
        return SignupRes(createMember(newMember).id!!)
    }

    @Transactional
    override fun signupFirebaseEmail(req: SignupFirebaseEmailReq): SignupRes {
        log.debug { "]-----] MemberServiceImpl::signupFirebaseEmail SignupFirebaseEmailReq[-----[ ${req}" }
        val exists = memberInfoRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, SnsType.EMAIL, true)
        if (exists != null) {
            throw SignupException(ErrorMessageCode.SNSID_ALREADY_EXISTS)
        }
        val newMember = MemberInfo(
            snsId = req.snsId,
            snsType = SnsType.EMAIL,
            nickName = req.nickName,
            phoneNumber = req.phoneNumber,
            agreeService = req.agreeService,
            agreeSecurity = req.agreeSecurity,
            agreeMarketing = req.agreeMarketing
        )
        return SignupRes(createMember(newMember).id!!)
    }

    @Transactional
    override fun signinFirebaseEmail(req: SigninReq): SignupRes {
        return memberInfoRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, SnsType.EMAIL, true)?.let { SignupRes(it.id!!, it.agreeMarketing) }
            ?: throw EntityNotFoundException("not found a Member(snsId = ${req.snsId})")
    }

    @Transactional
    override fun signinSns(req: SigninSnsReq): SignupRes {
        val newMember = MemberInfo(
            snsId = req.snsId,
            snsType = req.snsType,
            snsToken = req.snsToken,
            nickName = makeRandomUsername()
        )
        return memberInfoRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, req.snsType, true)?.let { SignupRes(it.id!!, it.agreeMarketing) }
            ?: SignupRes(createMember(newMember).id!!, false)
    }

    @Transactional(readOnly = true)
    override fun duplicateCheckBySnsId(duplicateCheckReq: DuplicateCheckReq): DuplicateCheckRes {
        log.debug { "]-----] MemberServiceImpl::duplicateCheckBySnsId duplicateCheckReq[-----[ ${duplicateCheckReq}" }
        memberInfoRepository.findBySnsIdAndSnsTypeAndActive(duplicateCheckReq.snsId, SnsType.EMAIL, true)
            ?: return DuplicateCheckRes(isDuplicated = false)
        return DuplicateCheckRes(
            isDuplicated = true
        )
    }

    @Transactional(readOnly = true)
    override fun duplicateCheckByNickName(duplicateCheckNickNameReq: DuplicateCheckNickNameReq): DuplicateCheckRes {
        log.debug { "]-----] MemberServiceImpl::duplicateCheckByNickName duplicateCheckReq[-----[ ${duplicateCheckNickNameReq}" }
        memberInfoRepository.findByNickNameAndActive(duplicateCheckNickNameReq.nickName, true)
            ?: return DuplicateCheckRes(isDuplicated = false)
        return DuplicateCheckRes(
            isDuplicated = true
        )
    }

    @Transactional(readOnly = true)
    override fun findById(memberId: Long): MemberRes {
        return memberInfoRepository.findByIdAndActive(memberId, true)?.let { modelMapper.map(it, MemberRes::class.java) }
            ?: throw EntityNotFoundException("not found a Member(id = ${memberId})")
    }

    private fun createMember(newMember: MemberInfo): MemberInfo {
        val userRole = roleRepository.findByRoleType(RoleType.ROLE_USER)
            ?: throw EntityNotFoundException("not found a Role(type = ${RoleType.ROLE_USER.name})")
        return memberInfoRepository.save(newMember)
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

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<MemberRes> {
        val members = memberInfoRepository.findAllBySearch(pageable, queryParams)
        return members.map {
            modelMapper.map(it, MemberRes::class.java)
        }
    }
}
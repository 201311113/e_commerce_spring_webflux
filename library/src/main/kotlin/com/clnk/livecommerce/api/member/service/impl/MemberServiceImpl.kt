package com.clnk.livecommerce.api.member.service.impl

import com.clnk.livecommerce.api.exception.ErrorMessageCode
import com.clnk.livecommerce.api.exception.SignupException
import com.clnk.livecommerce.api.member.*
import com.clnk.livecommerce.api.member.repository.MemberRepository
import com.clnk.livecommerce.api.member.repository.MemberRoleRepository
import com.clnk.livecommerce.api.member.repository.RoleRepository
import com.clnk.livecommerce.api.member.service.MemberService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

private val log = KotlinLogging.logger {}

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val roleRepository: RoleRepository,
) : MemberService {
    @Transactional
    override fun signup(req: SignupReq): SignupRes {
        log.debug { "]-----] MemberServiceImpl::signup SignupReq[-----[ ${req}" }
        val exists = memberRepository.findBySnsIdAndSnsTypeAndActive(req.snsId, SnsType.EMAIL, true)

        if (exists != null) {
            throw SignupException(ErrorMessageCode.SNSID_ALREADY_EXISTS)
        }
        val newMember = Member(snsId = req.snsId, snsType = SnsType.EMAIL, password = req.password, nickName = req.nickName)
        memberRepository.save(newMember)
        val role = roleRepository.findByRoleType(RoleType.ROLE_USER)
            ?: throw EntityNotFoundException("not found a Role(type = ${RoleType.ROLE_USER.name})")
        memberRoleRepository.save(MemberRole(newMember, role))
        log.debug { "]-----] MemberServiceImpl::signup newMember[-----[ ${newMember}" }
        return SignupRes(newMember.id!!)
    }

    @Transactional(readOnly = true)
    override fun duplicateCheckBySnsId(duplicateCheckReq: DuplicateCheckReq): DuplicateCheckRes {
        return DuplicateCheckRes(
            isDuplicated = false
        )
    }
}
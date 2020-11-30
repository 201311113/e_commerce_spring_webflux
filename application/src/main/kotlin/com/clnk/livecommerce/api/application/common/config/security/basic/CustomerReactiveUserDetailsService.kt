package com.clnk.livecommerce.api.application.common.config.security.basic

import com.clnk.livecommerce.api.member.SnsType
import com.clnk.livecommerce.api.member.repository.MemberRepository
import mu.KotlinLogging
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

@Service
class CustomerReactiveUserDetailsService(
    private val memberRepository: MemberRepository
) : ReactiveUserDetailsService {
    @Transactional(readOnly = true)
    override fun findByUsername(username: String?): Mono<UserDetails> {
        val member = memberRepository.findBySnsIdAndSnsTypeAndActive(username!!, SnsType.EMAIL, true)
            ?: throw BadCredentialsException("Invalid Credentials")
        log.debug { "]-----] CustomerReactiveUserDetailsService::findByUsername.member[-----[ ${member}" }
        val authorities = member.roles.map { memberRole -> SimpleGrantedAuthority(memberRole.role.roleType.name) }
        return Mono.just(User(member.id!!.toString(), member.password!!, authorities))
    }
}
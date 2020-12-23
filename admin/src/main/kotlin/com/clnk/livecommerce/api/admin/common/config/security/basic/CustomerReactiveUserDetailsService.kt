package com.clnk.livecommerce.api.admin.common.config.security.basic

import com.clnk.livecommerce.api.library.adminmember.repository.AdminMemberRepository
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
    private val adminMemberRepository: AdminMemberRepository
) : ReactiveUserDetailsService {
    @Transactional(readOnly = true)
    override fun findByUsername(username: String?): Mono<UserDetails> {
        val admin = adminMemberRepository.findByUsernameAndActive(username!!, true)
            ?: throw BadCredentialsException("Invalid Credentials")
        log.debug { "]-----] CustomerReactiveUserDetailsService::findByUsername.admin[-----[ ${admin}" }
        val authorities = admin.roles.map { memberRole -> SimpleGrantedAuthority(memberRole.roleAdmin.roleType.name) }
        return Mono.just(User(admin.id!!.toString(), admin.password!!, authorities))
    }
}
package com.clnk.livecommerce.api.application.member

import com.clnk.livecommerce.api.library.member.service.MemberService
import mu.KotlinLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok

private val log = KotlinLogging.logger {}

@Component
class MemberHandler(
    private val memberService: MemberService

) {
    suspend fun findMe(request: ServerRequest): ServerResponse {
        val memberId = request.awaitPrincipal()!!.name.toLong()
        return memberService.findById(memberId).let {
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
        }
    }

}
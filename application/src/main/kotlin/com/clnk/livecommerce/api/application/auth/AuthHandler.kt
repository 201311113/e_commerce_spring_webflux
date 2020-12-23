package com.clnk.livecommerce.api.application.auth

import com.clnk.livecommerce.api.application.auth.service.AuthService
import com.clnk.livecommerce.api.application.common.exception.SigninSnsException
import com.clnk.livecommerce.api.library.exception.ApiError
import com.clnk.livecommerce.api.library.exception.ValueNotValidException
import com.clnk.livecommerce.api.library.member.DuplicateCheckReq
import com.clnk.livecommerce.api.library.member.SigninSnsReq
import com.clnk.livecommerce.api.library.member.SignupReq
import com.clnk.livecommerce.api.library.member.service.MemberService
import mu.KotlinLogging
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import javax.validation.Validator

private val log = KotlinLogging.logger {}

@Component
class AuthHandler(
    private val memberService: MemberService,
    private val authService: AuthService,
    private val passwordEncoder: PasswordEncoder,
    private val validator: Validator
) {
    suspend fun signup(request: ServerRequest): ServerResponse {
        val signupReq = request.awaitBody<SignupReq>()
        validate(signupReq)
        return memberService.signup(signupReq.also { it.password = passwordEncoder.encode(it.password) })
            .let {
                ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
            }
    }

    private fun validate(signupReq: SignupReq) {
        val result = validator.validate(signupReq)
        if (result.size > 0) {
            var bindErrors = result.map { valid -> ApiError(code = valid.propertyPath.toString(), message = valid.message) }
            throw ValueNotValidException(bindErrors)
        }
    }

    suspend fun signin(request: ServerRequest): ServerResponse {
        log.debug { "]-----] AuthHandler::signin [-----[ call " }
//        val signupReq = request.awaitBody<SigninReq>()
//        validate(signupReq)
        return ok().contentType(APPLICATION_JSON).bodyValueAndAwait("Hello World")

    }

    suspend fun signinSns(request: ServerRequest): ServerResponse {
        log.debug { "]-----] AuthHandler::signin [-----[ call " }
        val signinReq = request.awaitBody<SigninSnsReq>()

        return if (authService.verifyAccessToken(signinReq.snsToken, signinReq.snsType, signinReq.snsId)) {
            val member = memberService.signinSns(signinReq)
            ok().contentType(APPLICATION_JSON).bodyValueAndAwait(authService.getAccessToken(member.id!!))
        } else {
            throw SigninSnsException()
        }


    }

    suspend fun duplicateCheckBySnsId(request: ServerRequest): ServerResponse {
        val duplicateCheckReq = request.awaitBody<DuplicateCheckReq>()
        return memberService.duplicateCheckBySnsId(duplicateCheckReq)
            .let {
                ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
            }
    }

}
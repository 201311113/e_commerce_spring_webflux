package com.clnk.livecommerce.api.application.auth

import com.clnk.livecommerce.api.exception.ApiError
import com.clnk.livecommerce.api.exception.ValueNotValidException
import com.clnk.livecommerce.api.member.DuplicateCheckReq
import com.clnk.livecommerce.api.member.SigninReq
import com.clnk.livecommerce.api.member.SignupReq
import com.clnk.livecommerce.api.member.service.MemberService
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
//        val errors: Errors = BeanPropertyBindingResult(signupReq, signupReq::class.simpleName as String);
        val result = validator.validate(signupReq)
        log.debug { "]-----] AuthHandler::validate result [-----[ ${result}" }
        log.debug { "]-----] AuthHandler::validate result [-----[ ${result.size}" }
        if (result.size > 0) {
            var bindErrors = result.map { valid -> ApiError(code = valid.propertyPath.toString(), message = valid.message) }
//                .apply { errors = bindErrors }
//            val exception = ValueNotValidException()
//            exception.errors = bindErrors
            throw ValueNotValidException(bindErrors)
        }
    }

    suspend fun signin(request: ServerRequest): ServerResponse {
        log.debug { "]-----] AuthHandler::signin [-----[ call " }
//        val signupReq = request.awaitBody<SigninReq>()
//        validate(signupReq)
        return ok().contentType(APPLICATION_JSON).bodyValueAndAwait("Hello World")

    }

    suspend fun duplicateCheckBySnsId(request: ServerRequest): ServerResponse {
        val duplicateCheckReq = request.awaitBody<DuplicateCheckReq>()
        return memberService.duplicateCheckBySnsId(duplicateCheckReq)
            .let {
                ok().contentType(APPLICATION_JSON).bodyValueAndAwait(it)
            }
    }

}
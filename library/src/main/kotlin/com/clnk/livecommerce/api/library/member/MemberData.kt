package com.clnk.livecommerce.api.library.member

import java.time.Instant
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

const val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^()])[a-zA-Z0-9!@#$%^()]{8,25}$"
const val nickNameRegex = "^[a-zA-Z0-9ㄱ-ㅎ가-힣]{2,25}$"

data class SignupReq(
    @get:Email
    @get:NotBlank
    var snsId: String,
    @get:NotBlank
    @get:Pattern(regexp = passwordRegex)
    var password: String,
    @get:NotBlank
    @get:Pattern(regexp = passwordRegex)
    var passwordRepeat: String,
    @get:NotBlank
    @get:Pattern(regexp = nickNameRegex)
    var nickName: String,
)

data class SignupRes(
    var id: Long = -1
)

data class SigninReq(
    @get:Email
    @get:NotBlank
    var snsId: String,
    @field:NotNull
    var snsType: SnsType,
    @get:NotBlank
    @get:Pattern(regexp = passwordRegex)
    var password: String
)

data class DuplicateCheckReq(
    @get:Email
    @get:NotBlank
    var snsId: String,
)

data class DuplicateCheckRes(
    var isDuplicated: Boolean,
)

data class MemberRes(
    var id: Long = -1,
    var snsId: String? = null,
    var snsType: SnsType? = null,
    var status: MemberStatus? = null,
    var nickName: String? = null,
    var realName: String? = null,
    var createdAt: Instant? = null
)

data class SigninSnsReq(
    @get:NotBlank
    var snsId: String,
    @field:NotNull
    var snsType: SnsType,
    var snsToken: String = "",
    var username: String? = "",
    var email: String? = "",
    var nickName: String? = "",
    var gender: Gender? = null
)


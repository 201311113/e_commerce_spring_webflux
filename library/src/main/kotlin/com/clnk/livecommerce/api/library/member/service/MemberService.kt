package com.clnk.livecommerce.api.library.member.service

import com.clnk.livecommerce.api.library.member.*

interface MemberService {
    fun signup(signupReq: SignupReq): SignupRes
    fun signinSns(signinSnsReq: SigninSnsReq): SignupRes
    fun duplicateCheckBySnsId(duplicateCheckReq: DuplicateCheckReq): DuplicateCheckRes
    fun findById(memberId: Long): MemberRes
}
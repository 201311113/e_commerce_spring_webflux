package com.clnk.livecommerce.api.library.member.service

import com.clnk.livecommerce.api.library.member.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap

interface MemberService {
    fun signup(signupReq: SignupReq): SignupRes
    fun signupFirebaseEmail(signupReq: SignupFirebaseEmailReq): SignupRes
    fun signinFirebaseEmail(signupReq: SigninReq): SignupRes
    fun signinSns(signinSnsReq: SigninSnsReq): SignupRes
    fun duplicateCheckBySnsId(duplicateCheckReq: DuplicateCheckReq): DuplicateCheckRes
    fun duplicateCheckByNickName(duplicateCheckNickNameReq: DuplicateCheckNickNameReq): DuplicateCheckRes
    fun findById(memberId: Long): MemberRes
    fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<MemberRes>
}
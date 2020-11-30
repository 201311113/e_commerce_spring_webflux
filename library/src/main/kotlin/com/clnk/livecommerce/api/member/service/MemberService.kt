package com.clnk.livecommerce.api.member.service

import com.clnk.livecommerce.api.member.DuplicateCheckReq
import com.clnk.livecommerce.api.member.DuplicateCheckRes
import com.clnk.livecommerce.api.member.SignupReq
import com.clnk.livecommerce.api.member.SignupRes

interface MemberService {
    fun signup(signupReq: SignupReq): SignupRes
    fun duplicateCheckBySnsId(duplicateCheckReq: DuplicateCheckReq): DuplicateCheckRes
}
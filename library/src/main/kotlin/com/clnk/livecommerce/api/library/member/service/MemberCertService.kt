package com.clnk.livecommerce.api.library.member.service

import com.clnk.livecommerce.api.library.member.PhoneNumberVerifyReq
import com.clnk.livecommerce.api.library.member.PhoneNumberVerifyRes
import com.clnk.livecommerce.api.library.member.SendVerifyCodeReq
import com.clnk.livecommerce.api.library.member.SendVerifyCodeRes

interface MemberCertService {
    fun create(sendVerifyCodeReq: SendVerifyCodeReq): SendVerifyCodeRes
    fun verifyCode(phoneNumberVerifyReq: PhoneNumberVerifyReq): PhoneNumberVerifyRes

}
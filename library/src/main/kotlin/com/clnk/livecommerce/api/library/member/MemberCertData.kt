package com.clnk.livecommerce.api.library.member

data class SendVerifyCodeReq(
    var phoneNumber: String
)
data class SendVerifyCodeRes(
    var id: Long = -1,
)

data class PhoneNumberVerifyReq(
    var phoneNumber: String,
    var verifyCode: String,
)

data class PhoneNumberVerifyRes(
    var isVerified: Boolean,
)
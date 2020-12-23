package com.clnk.livecommerce.api.application.common.exception

enum class ApplicationErrorMessageCode(val code: Int, val message: String) {
    DUMMY(9999, "dummy"),
    SIGNIN_IS_NOT_ALLOWED(1000, "signin is not allowed"),
    SNSID_IS_REQUIRED(1001, "snsid is required"),
    SNSTYPE_IS_MISMATCHED(1002, "snstype is mismatched"),
    SNSTOKEN_IS_NOT_ACCEPTABLE(1003, "snstoken is not acceptable"),
}
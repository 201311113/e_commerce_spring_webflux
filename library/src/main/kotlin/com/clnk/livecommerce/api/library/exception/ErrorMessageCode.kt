package com.clnk.livecommerce.api.library.exception

enum class ErrorMessageCode(val code: String, val message: String) {
    OK("0000", "ok"),
    DUMMY("9998", "dummy"),
    ERROR("9999", "Internal Server Error"),
    LOGIC_ERROR("9000", "Internal Server Error"),
    NOT_FOUND("9404", "Not Found"),
    VALUE_NOT_VALID("9500", "Value Not Valid"),

    SIGNIN_NOT_ALLOWED("1000", "signin not allowed"),
    SNSID_REQUIRED("1001", "snsid required"),
    SNSTYPE_MISMATCHED("1002", "snstype mismatched"),
    SNSTOKEN_NOT_ACCEPTABLE("1003", "snstoken not acceptable"),
    SNSID_ALREADY_EXISTS("1004", "snsid already exists"),

    PRODUCT_NOT_FOUND("1500", "product not found"),
    PRODUCT_NAME_REQUIRED("1501", "product name required"),
    PRODUCT_DESCRIPTION_REQUIRED("1502", "product description required"),
    PRODUCT_IMAGE_REQUIRED("1503", "product image required"),
    PRODUCT_BRAND_REQUIRED("1504", "brand required"),
    FILE_NOT_FOUND("1505", "file not found"),

    BROADCAST_TITLE_REQUIRED("1600", "broadcast title required"),
    BROADCAST_DESCRIPTION_REQUIRED("1601", "broadcast description required"),
    BROADCAST_STARTAT_REQUIRED("1601", "broadcast startat required"),
    BROADCAST_ENDAT_REQUIRED("1601", "broadcast endat required"),
    BROADCAST_ONSALEITEMS_REQUIRED("1601", "broadcast onsaleitems required"),


    ORDER_PRICE_NOT_ALLOWED("2001", "Order Price Not Allowed"),
    ORDER_PRICE_NOT_MATCHED("2002", "Order Price Not Matched"),
    TOTAL_PRICE_NOT_ALLOWED("2003", "Total Price Not Allowed"),
    PAYMENT_NOT_VERIFIED("2004", "Payment not verified"),
}
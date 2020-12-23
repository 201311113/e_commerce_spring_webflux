package com.clnk.livecommerce.api.library.exception

open class ApplicationException(val errorMessageCode: ErrorMessageCode) : RuntimeException(errorMessageCode.message) {
    open var errors: List<ApiError>? = null
        set(value) {
            errors = value
        }
}

class NotFoundException : ApplicationException(ErrorMessageCode.NOT_FOUND)
class LogicalException : ApplicationException(ErrorMessageCode.LOGIC_ERROR)
class SignupException(errorMessageCode: ErrorMessageCode) : ApplicationException(errorMessageCode)
class SigninException(errorMessageCode: ErrorMessageCode) : ApplicationException(errorMessageCode)
class ProductException(errorMessageCode: ErrorMessageCode) : ApplicationException(errorMessageCode)
class MediaUtilsException(errorMessageCode: ErrorMessageCode) : ApplicationException(errorMessageCode)
class BroadcastException(errorMessageCode: ErrorMessageCode) : ApplicationException(errorMessageCode)

class OrderException(errorMessageCode: ErrorMessageCode) : ApplicationException(errorMessageCode)

class ValueNotValidException(override var errors: List<ApiError>?) : ApplicationException(ErrorMessageCode.VALUE_NOT_VALID)


data class ApiError(
    var code: String,
    var message: String
)
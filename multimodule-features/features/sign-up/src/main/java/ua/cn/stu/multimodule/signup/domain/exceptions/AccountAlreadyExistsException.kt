package ua.cn.stu.multimodule.signup.domain.exceptions

import ua.cn.stu.multimodule.core.AppException

class AccountAlreadyExistsException(
    cause: Throwable? = null
) : AppException(cause = cause)
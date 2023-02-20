package ua.cn.stu.multimodule.signup.domain.exceptions

import ua.cn.stu.multimodule.core.AppException
import ua.cn.stu.multimodule.signup.domain.entities.SignUpField

class EmptyFieldException(
    val field: SignUpField,
) : AppException()
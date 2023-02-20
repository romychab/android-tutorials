package ua.cn.stu.multimodule.orders.domain.exceptions

import ua.cn.stu.multimodule.core.AppException
import ua.cn.stu.multimodule.orders.domain.entities.Field

class EmptyFieldException(
    val field: Field
) : AppException()
package ua.cn.stu.multimodule.glue.orders.mappers

import ua.cn.stu.multimodule.data.orders.entities.RecipientDataEntity
import ua.cn.stu.multimodule.orders.domain.entities.Recipient
import javax.inject.Inject

class RecipientMapper @Inject constructor() {

    fun toRecipientDataEntity(recipient: Recipient): RecipientDataEntity {
        return RecipientDataEntity(
            firstName = recipient.firstName,
            lastName = recipient.lastName,
            address = recipient.address,
        )
    }
}
package ua.cn.stu.tests.data.accounts.entities

import org.junit.Assert.assertEquals
import org.junit.Test
import ua.cn.stu.tests.domain.accounts.entities.Account

class GetAccountResponseEntityTest {

    @Test
    fun toAccountMapsToInAppEntity() {
        val responseEntity = GetAccountResponseEntity(
            id = 3,
            email = "some-email",
            username = "some-username",
            createdAt = 123
        )

        val inAppEntity = responseEntity.toAccount()

        val expectedInAppEntity = Account(
            id = 3,
            email = "some-email",
            username = "some-username",
            createdAt = 123
        )
        assertEquals(expectedInAppEntity, inAppEntity)
    }
}
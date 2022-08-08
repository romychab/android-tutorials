package ua.cn.stu.tests.domain.accounts.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class AccountTest {

    @Test
    fun newInstanceUsesUnknownCreatedAtValue() {
        val account = Account(
            id = 1,
            username = "username",
            email = "email"
        )

        val createdAt = account.createdAt

        assertEquals(Account.UNKNOWN_CREATED_AT, createdAt)
    }
}
package ua.cn.stu.navcomponent.tabs

import ua.cn.stu.navcomponent.tabs.model.accounts.AccountsRepository
import ua.cn.stu.navcomponent.tabs.model.accounts.InMemoryAccountsRepository
import ua.cn.stu.navcomponent.tabs.model.boxes.BoxesRepository
import ua.cn.stu.navcomponent.tabs.model.boxes.InMemoryBoxesRepository

object Repositories {

    val accountsRepository: AccountsRepository = InMemoryAccountsRepository()

    val boxesRepository: BoxesRepository = InMemoryBoxesRepository(accountsRepository)

}
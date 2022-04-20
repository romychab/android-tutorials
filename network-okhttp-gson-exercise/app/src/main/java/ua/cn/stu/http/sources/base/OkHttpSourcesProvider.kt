package ua.cn.stu.http.sources.base

import ua.cn.stu.http.app.model.SourcesProvider
import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.boxes.BoxesSource
import ua.cn.stu.http.sources.accounts.OkHttpAccountsSource
import ua.cn.stu.http.sources.boxes.OkHttpBoxesSource

// todo #8: create AccountsSource and BoxesSource.
class OkHttpSourcesProvider(
    private val config: OkHttpConfig
) : SourcesProvider {

    override fun getAccountsSource(): AccountsSource {
        TODO("Create an instance of OkHttpAccountsSource")
    }

    override fun getBoxesSource(): BoxesSource {
        TODO("Create an instance of OkHttpBoxesSource")
    }

}
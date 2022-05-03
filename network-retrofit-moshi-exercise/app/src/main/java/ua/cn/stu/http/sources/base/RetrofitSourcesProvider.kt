package ua.cn.stu.http.sources.base

import ua.cn.stu.http.app.model.SourcesProvider
import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.boxes.BoxesSource
import ua.cn.stu.http.sources.accounts.RetrofitAccountsSource
import ua.cn.stu.http.sources.boxes.RetrofitBoxesSource

// todo #9: create AccountsSource and BoxesSource.
class RetrofitSourcesProvider(
    private val config: RetrofitConfig
) : SourcesProvider {

    override fun getAccountsSource(): AccountsSource {
        TODO()
    }

    override fun getBoxesSource(): BoxesSource {
        TODO()
    }

}
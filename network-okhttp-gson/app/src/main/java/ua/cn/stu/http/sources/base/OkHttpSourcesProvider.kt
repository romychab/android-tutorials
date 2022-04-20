package ua.cn.stu.http.sources.base

import ua.cn.stu.http.app.model.SourcesProvider
import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.boxes.BoxesSource
import ua.cn.stu.http.sources.accounts.OkHttpAccountsSource
import ua.cn.stu.http.sources.boxes.OkHttpBoxesSource

/**
 * Creating sources based on OkHttp + GSON.
 */
class OkHttpSourcesProvider(
    private val config: OkHttpConfig
) : SourcesProvider {

    override fun getAccountsSource(): AccountsSource {
        return OkHttpAccountsSource(config)
    }

    override fun getBoxesSource(): BoxesSource {
        return OkHttpBoxesSource(config)
    }

}
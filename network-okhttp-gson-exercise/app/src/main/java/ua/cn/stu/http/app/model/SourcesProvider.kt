package ua.cn.stu.http.app.model

import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.boxes.BoxesSource

/**
 * Factory class for all network sources.
 */
interface SourcesProvider {

    /**
     * Create [AccountsSource] which is responsible for reading/writing
     * user accounts data.
     */
    fun getAccountsSource(): AccountsSource

    /**
     * Create [BoxesSource] which is responsible for reading/updating
     * boxes data.
     */
    fun getBoxesSource(): BoxesSource

}
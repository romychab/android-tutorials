package ua.cn.stu.http.app

import android.content.Context
import ua.cn.stu.http.app.model.settings.SharedPreferencesAppSettings
import ua.cn.stu.http.app.model.SourcesProvider
import ua.cn.stu.http.app.model.accounts.AccountsRepository
import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.boxes.BoxesRepository
import ua.cn.stu.http.app.model.boxes.BoxesSource
import ua.cn.stu.http.app.model.settings.AppSettings
import ua.cn.stu.http.sources.SourceProviderHolder

object Singletons {

    private lateinit var appContext: Context

    private val sourcesProvider: SourcesProvider by lazy {
        SourceProviderHolder.sourcesProvider
    }

    val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(appContext)
    }

    // --- sources

    private val accountsSource: AccountsSource by lazy {
        sourcesProvider.getAccountsSource()
    }

    private val boxesSource: BoxesSource by lazy {
        sourcesProvider.getBoxesSource()
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        AccountsRepository(
            accountsSource = accountsSource,
            appSettings = appSettings
        )
    }

    val boxesRepository: BoxesRepository by lazy {
        BoxesRepository(
            accountsRepository = accountsRepository,
            boxesSource = boxesSource
        )
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(appContext: Context) {
        Singletons.appContext = appContext
    }
}


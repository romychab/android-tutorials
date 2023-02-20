package ua.cn.stu.multimodule.core

import kotlinx.coroutines.CoroutineScope

/**
 * Common global singleton variables.
 * You need to call [Core.init] before any other interactions with core.
 *
 * `Application.onCreate()` is a good place to do so.
 */
object Core {

    private lateinit var coreProvider: CoreProvider

    /**
     * @see CommonUi
     */
    val commonUi: CommonUi get() = coreProvider.commonUi

    /**
     * @see Resources
     */
    val resources: Resources get() = coreProvider.resources

    /**
     * @see Logger
     */
    val logger: Logger get() = coreProvider.logger

    /**
     * Global scope for launching async actions which results don't matter and
     * can be ignored. Exceptions are ignored too.
     */
    val globalScope: CoroutineScope get() = coreProvider.globalScope

    /**
     * Default global error handler for the whole app. Usually it is used
     * in a viewModelScope to handle basic errors.
     * @see ErrorHandler
     */
    val errorHandler: ErrorHandler get() = coreProvider.errorHandler

    /**
     * An interface which provides a method for restarting the app.
     */
    val appRestarter: AppRestarter get() = coreProvider.appRestarter

    /**
     * Communication interface for sending and receiving screen results.
     */
    val screenCommunication: ScreenCommunication get() = coreProvider.screenCommunication

    /**
     * Default timeout for local operations.
     */
    val localTimeoutMillis: Long get() = coreProvider.localTimeoutMillis

    /**
     * Default timeout for remote operations.
     */
    val remoteTimeoutMillis: Long get() = coreProvider.remoteTimeoutMillis

    /**
     * Default debounce period for UI actions.
     */
    val debouncePeriodMillis: Long get() = coreProvider.debouncePeriodMillis

    /**
     * Init this core.
     */
    fun init(coreProvider: CoreProvider) {
        this.coreProvider = coreProvider
    }

}
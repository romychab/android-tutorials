package ua.cn.stu.foundation

import android.content.Context
import androidx.annotation.MainThread
import ua.cn.stu.foundation.views.ViewModelFactory

typealias SingletonsFactory = (applicationContext: Context) -> List<Any>

/**
 * Simple DI container for singleton dependencies.
 * It holds the list of singletons that can be injected to view-models by [ViewModelFactory].
 * The list of dependencies are created by [SingletonsFactory] provided via [init] call.
 * Factory block is executed only once upon the first request of singletons. You need to call
 * [init] method in all entry points of your application and provide the initialization block
 * which should return the list of singletons.
 */
object SingletonScopeDependencies {

    private var factory: SingletonsFactory? = null
    private var dependencies: List<Any>? = null

    /**
     * Set the init factory block which creates and returns the list of all singletons.
     */
    @MainThread
    fun init(factory: SingletonsFactory) {
        if (this.factory != null) return
        this.factory = factory
    }

    /**
     * Get the list of all singletons provided by the factory specified by [init] call.
     * Method [init] should be called before.
     * @throws [IllegalStateException] if factory hasn't been assigned via [init] before.
     */
    @MainThread
    fun getSingletonScopeDependencies(applicationContext: Context): List<Any> {
        val factory = this.factory ?: throw IllegalStateException("Call init() before getting singleton dependencies")
        return dependencies ?: factory(applicationContext).also { this.dependencies = it }
    }

}
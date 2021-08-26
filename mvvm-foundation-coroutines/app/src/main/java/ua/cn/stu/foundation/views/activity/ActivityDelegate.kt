package ua.cn.stu.foundation.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import ua.cn.stu.foundation.ActivityScopeViewModel
import ua.cn.stu.foundation.sideeffects.SideEffectImplementationsHolder
import ua.cn.stu.foundation.sideeffects.SideEffectPlugin
import ua.cn.stu.foundation.sideeffects.SideEffectPluginsManager
import ua.cn.stu.foundation.utils.viewModelCreator

/**
 * Delegate that manages side-effect plugins and contains common logic.
 * The following methods should be called from activity:
 * - [onBackPressed]
 * - [onSupportNavigateUp]
 * - [onCreate]
 * - [onSavedInstanceState]
 * - [onActivityResult]
 * - [onRequestPermissionsResult]
 */
class ActivityDelegate(
    private val activity: AppCompatActivity
) : LifecycleObserver {

    internal val sideEffectPluginsManager = SideEffectPluginsManager()

    private val activityViewModel by activity.viewModelCreator<ActivityScopeViewModel> { ActivityScopeViewModel() }

    private val implementersHolder = SideEffectImplementationsHolder()

    init {
        activity.lifecycle.addObserver(this)
    }

    /**
     * Call this method from [AppCompatActivity.onBackPressed].
     * Example:
     * ```
     * override fun onBackPressed() {
     *     if (!delegate.onBackPressed()) super.onBackPressed()
     * }
     * ```
     */
    fun onBackPressed(): Boolean {
        return implementersHolder.implementations.any { it.onBackPressed() }
    }

    /**
     * Call this method from [AppCompatActivity.onSupportNavigateUp]
     * If this method returns `null` you should call `super.onSupportNavigateUp()` if your activity.
     * Example:
     * ```
     * override fun onSupportNavigateUp(): Boolean {
     *    return delegate.onSupportNavigateUp() ?: super.onSupportNavigateUp()
     * }
     * ```
     */
    fun onSupportNavigateUp(): Boolean? {
        for (service in implementersHolder.implementations) {
            val value = service.onSupportNavigateUp()
            if (value != null) {
                return value
            }
        }
        return null
    }

    /**
     * Call this method from [AppCompatActivity.onCreate].
     */
    fun onCreate(savedInstanceState: Bundle?) {
        sideEffectPluginsManager.plugins.forEach { plugin ->
            setupSideEffectMediator(plugin)
            setupSideEffectImplementer(plugin)
        }
        implementersHolder.implementations.forEach { it.onCreate(savedInstanceState) }
    }

    /**
     * Call this method from [AppCompatActivity.onSaveInstanceState]
     */
    fun onSavedInstanceState(outState: Bundle) {
        implementersHolder.implementations.forEach { it.onSaveInstanceState(outState) }
    }

    /**
     * Call this method from [AppCompatActivity.onActivityResult]
     */
    fun onActivityResult(requestCode: Int, responseCode: Int, data: Intent?) {
        implementersHolder.implementations.forEach { it.onActivityResult(requestCode, responseCode, data) }
    }

    /**
     * Call this method from [AppCompatActivity.onRequestPermissionsResult]
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, granted: IntArray) {
        implementersHolder.implementations.forEach { it.onRequestPermissionsResult(requestCode, permissions, granted) }
    }

    fun notifyScreenUpdates() {
        implementersHolder.implementations.forEach { it.onRequestUpdates() }
    }

    fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        sideEffectPluginsManager.plugins.forEach {
            activityViewModel.sideEffectMediatorsHolder.setTargetWithPlugin(it, implementersHolder)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        activityViewModel.sideEffectMediatorsHolder.removeTargets()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        implementersHolder.clear()
    }

    private fun setupSideEffectMediator(plugin: SideEffectPlugin<*, *>) {
        val holder = activityViewModel.sideEffectMediatorsHolder

        if (!holder.contains(plugin.mediatorClass)) {
            holder.putWithPlugin(activity.applicationContext, plugin)
        }
    }

    private fun setupSideEffectImplementer(plugin: SideEffectPlugin<*, *>) {
        implementersHolder.putWithPlugin(plugin, activityViewModel.sideEffectMediatorsHolder, activity)
    }

}
package ua.cn.stu.foundation.sideeffects

import android.content.Context

/**
 * Entry point for each side-effect plugin.
 */
interface SideEffectPlugin<Mediator, Implementation> {

    /**
     * Class of side-effect mediator.
     */
    val mediatorClass: Class<Mediator>

    /**
     * Create a mediator instance which acts on view-model side.
     */
    fun createMediator(applicationContext: Context): SideEffectMediator<Implementation>

    /**
     * Create an implementation for the mediator created by [createMediator] method.
     * May return `null`. NULL-value may be useful if logic can be implemented directly in mediator
     * (e.g. side-effect doesn't need activity instance)
     */
    fun createImplementation(mediator: Mediator): Implementation? = null

}
package ua.cn.stu.foundation.sideeffects

/**
 * Manager that holds all side-effect plugins.
 */
class SideEffectPluginsManager {

    private val _plugins = mutableListOf<SideEffectPlugin<*, *>>()
    internal val plugins: List<SideEffectPlugin<*, *>>
        get() = _plugins

    /**
     * Register a new side-effect plugin.
     * Mediator interface specified by the plugin can be used in view-models' constructor.
     */
    fun <Mediator, Implementation> register(plugin: SideEffectPlugin<Mediator, Implementation>) {
        _plugins.add(plugin)
    }

}
package ua.cn.stu.foundation.utils

typealias ResourceAction<T> = (T) -> Unit

/**
 * Actions queue, where actions are executed only if resource exists. If it doesn't then
 * action is added to queue and waits until resource becomes available.
 */
class ResourceActions<T> {

    var resource: T? = null
        set(newValue) {
            field = newValue
            if (newValue != null) {
                actions.forEach { it(newValue) }
                actions.clear()
            }
        }

    private val actions = mutableListOf<ResourceAction<T>>()

    /**
     * Invoke the action only when [resource] exists (not null). Otherwise
     * the action is postponed until some non-null value is assigned to [resource]
     */
    operator fun invoke(action: ResourceAction<T>) {
        val resource = this.resource
        if (resource == null) {
            actions += action
        } else {
            action(resource)
        }
    }

    fun clear() {
        actions.clear()
    }
}
package ua.cn.stu.foundation.model.tasks.dispatchers

/**
 * Dispatchers run the specified block of code in some way.
 */
interface Dispatcher {

    fun dispatch(block: () -> Unit)

}
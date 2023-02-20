package ua.cn.stu.multimodule.core.entities

/**
 * Use this class in MutableStateFlow if you want to notify
 * about changes in mutable values.
 *
 * Usage example:
 *
 * ```
 *   val myList = mutableListOf("one", "two")
 *   val stateFlow = MutableStateFlow(OnChange(myList))
 *
 *   launch {
 *     stateFlow.collect {
 *       println(it.size)
 *     }
 *   }
 *
 *   myList.add("three")
 *   stateFlow.value = OnChange(myList)
 * ```
 *
 * Output:
 * ```
 * 2
 * 3
 * ```
 */
class OnChange<T>(
    val value: T
)
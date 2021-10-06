package ua.cn.stu.navcomponent

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

typealias ResultsListener<T> = (T) -> Unit

/**
 * Send some results to the previous fragment.
 */
fun <T> Fragment.publishResults(key: String, result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

/**
 * Listen for screen results. Results are automatically cleared when the listener receives them.
 */
fun <T> Fragment.listenResults(key: String, listener: ResultsListener<T>) {
    val liveData = findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
    liveData?.observe(viewLifecycleOwner) { result ->
        if (result != null) {
            listener(result)
            liveData.value = null
        }
    }
}
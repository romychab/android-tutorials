package ua.cn.stu.recyclerview.screens

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.cn.stu.recyclerview.App
import ua.cn.stu.recyclerview.Navigator

typealias ViewModelCreator = (App) -> ViewModel?

class ViewModelFactory(
    private val app: App,
    private val viewModelCreator: ViewModelCreator = { null }
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            UsersListViewModel::class.java -> {
                UsersListViewModel(app.usersService)
            }
            else -> {
                viewModelCreator(app) ?: throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator

/**
 * Use this method in fragments when you want to create a view-model directly by constructor.
 *
 * Usage example:
 *
 * ```
 * class MyFragment : Fragment() {
 *   val viewModel: MyViewModel by viewModelCreator { MyViewModel(...) }
 * }
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelCreator(noinline creator: ViewModelCreator): Lazy<VM> {
    return viewModels { ViewModelFactory(requireContext().applicationContext as App, creator) }
}

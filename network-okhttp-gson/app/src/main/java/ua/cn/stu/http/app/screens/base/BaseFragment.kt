package ua.cn.stu.http.app.screens.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.utils.findTopNavController
import ua.cn.stu.http.app.utils.observeEvent

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showErrorMessageEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.showErrorMessageResEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.showAuthErrorAndRestartEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.auth_error, Toast.LENGTH_SHORT).show()
            logout()
        }
    }

    fun logout() {
        viewModel.logout()
        restartWithSignIn()
    }

    private fun restartWithSignIn() {
        findTopNavController().navigate(R.id.signInFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }

}
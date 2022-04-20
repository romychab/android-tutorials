package ua.cn.stu.http.app.screens.main.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.databinding.FragmentSignInBinding
import ua.cn.stu.http.app.screens.base.BaseFragment
import ua.cn.stu.http.app.utils.observeEvent

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    override val viewModel by viewModels<SignInViewModel>()

    private lateinit var binding: FragmentSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        binding.signInButton.setOnClickListener { onSignInButtonPressed() }
        binding.signUpButton.setOnClickListener { onSignUpButtonPressed() }

        observeState()
        observeClearPasswordEvent()
        observeShowAuthErrorMessageEvent()
        observeNavigateToTabsEvent()
    }

    private fun onSignInButtonPressed() {
        viewModel.signIn(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
        binding.emailTextInput.error = if (it.emptyEmailError) getString(R.string.field_is_empty) else null
        binding.passwordTextInput.error = if (it.emptyPasswordError) getString(R.string.field_is_empty) else null

        binding.emailTextInput.isEnabled = it.enableViews
        binding.passwordTextInput.isEnabled = it.enableViews
        binding.signInButton.isEnabled = it.enableViews
        binding.signUpButton.isEnabled = it.enableViews
        binding.progressBar.visibility = if (it.showProgress) View.VISIBLE else View.INVISIBLE
    }

    private fun observeShowAuthErrorMessageEvent() = viewModel.showAuthToastEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private fun observeClearPasswordEvent() = viewModel.clearPasswordEvent.observeEvent(viewLifecycleOwner) {
        binding.passwordEditText.text?.clear()
    }

    private fun observeNavigateToTabsEvent() = viewModel.navigateToTabsEvent.observeEvent(viewLifecycleOwner) {
        findNavController().navigate(R.id.action_signInFragment_to_tabsFragment)
    }

    private fun onSignUpButtonPressed() {
        val email = binding.emailEditText.text.toString()
        val emailArg = if (email.isBlank())
            null
        else {
            email
        }

        val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(emailArg)
        findNavController().navigate(direction)
    }

}
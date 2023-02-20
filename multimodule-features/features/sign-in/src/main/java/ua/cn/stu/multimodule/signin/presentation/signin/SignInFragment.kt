package ua.cn.stu.multimodule.signin.presentation.signin

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.multimodule.core.presentation.viewBinding
import ua.cn.stu.multimodule.core.presentation.views.observe
import ua.cn.stu.multimodule.signin.R
import ua.cn.stu.multimodule.signin.databinding.FragmentSignInBinding

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding<FragmentSignInBinding>()

    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupListeners()
            observeScreenState()
        }
    }

    private fun FragmentSignInBinding.setupListeners() {
        root.setTryAgainListener { viewModel.load() }
        signInButton.setOnClickListener {
            viewModel.signIn(emailEditText.text.toString(), passwordEditText.text.toString())
        }
        signUpButton.setOnClickListener {
            viewModel.launchSignUp(emailEditText.text.toString())
        }
        emailEditText.addTextChangedListener {
            viewModel.clearEmailError()
        }
        passwordEditText.addTextChangedListener {
            viewModel.clearPasswordError()
        }
    }

    private fun FragmentSignInBinding.observeScreenState() {
        root.observe(viewLifecycleOwner, viewModel.stateLiveValue) { state ->
            signInButton.isEnabled = state.enableButtons
            signUpButton.isEnabled = state.enableButtons
            progressBar.isInvisible = !state.showProgressBar
            emailTextInput.error = state.emailError
            passwordTextInput.error = state.passwordError
        }
    }

}
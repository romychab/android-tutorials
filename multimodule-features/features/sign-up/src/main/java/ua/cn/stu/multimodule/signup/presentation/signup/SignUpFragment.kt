package ua.cn.stu.multimodule.signup.presentation.signup

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.multimodule.core.presentation.BaseScreen
import ua.cn.stu.multimodule.core.presentation.args
import ua.cn.stu.multimodule.core.presentation.live.observeEvent
import ua.cn.stu.multimodule.core.presentation.viewBinding
import ua.cn.stu.multimodule.core.presentation.viewModelCreator
import ua.cn.stu.multimodule.signup.R
import ua.cn.stu.multimodule.signup.databinding.FragmentSignUpBinding
import ua.cn.stu.multimodule.signup.domain.entities.SignUpData
import ua.cn.stu.multimodule.signup.domain.entities.SignUpField
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    class Screen(
        val email: String
    ) : BaseScreen

    @Inject lateinit var factory: SignUpViewModel.Factory
    private val viewModel by viewModelCreator {
        factory.create(args())
    }

    private val binding by viewBinding<FragmentSignUpBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvents(savedInstanceState)
        with(binding) {
            observeState()
            setupListeners()
        }
    }

    private fun observeEvents(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.initialEmailLiveEventValue.observeEvent(viewLifecycleOwner) {
                binding.emailEditText.setText(it)
            }
        }
        viewModel.clearFieldLiveEventValue.observeEvent(viewLifecycleOwner) {
            getEditTextByField(it).text.clear()
        }
        viewModel.focusFieldLiveEventValue.observeEvent(viewLifecycleOwner) {
            getEditTextByField(it).requestFocus()
            getEditTextByField(it).selectAll()
        }
    }

    private fun FragmentSignUpBinding.observeState() {
        viewModel.stateLiveValue.observe(viewLifecycleOwner) { state ->
            progressBar.isInvisible = !state.showProgressBar
            createAccountButton.isEnabled = state.enableSignUpButton

            cleanUpErrors()
            if (state.fieldErrorMessage != null) {
                val textInput = getTextInputByField(state.fieldErrorMessage.first)
                textInput.error = state.fieldErrorMessage.second
                textInput.isErrorEnabled = true
            }
        }
    }

    private fun FragmentSignUpBinding.setupListeners() {
        createAccountButton.setOnClickListener {
            viewModel.signUp(createSignUpData())
        }
        emailEditText.addTextChangedListener { viewModel.clearError(SignUpField.EMAIL) }
        usernameEditText.addTextChangedListener { viewModel.clearError(SignUpField.USERNAME) }
        passwordEditText.addTextChangedListener { viewModel.clearError(SignUpField.PASSWORD) }
        repeatPasswordEditText.addTextChangedListener { viewModel.clearError(SignUpField.REPEAT_PASSWORD) }
    }

    private fun FragmentSignUpBinding.createSignUpData(): SignUpData {
        return SignUpData(
            email = emailEditText.text.toString(),
            username = usernameEditText.text.toString(),
            password = passwordEditText.text.toString(),
            repeatPassword = repeatPasswordEditText.text.toString(),
        )
    }

    private fun FragmentSignUpBinding.cleanUpErrors() {
        emailTextInput.error = null
        usernameTextInput.error = null
        passwordTextInput.error = null
        repeatPasswordTextInput.error = null
        emailTextInput.isErrorEnabled = false
        usernameTextInput.isErrorEnabled = false
        passwordTextInput.isErrorEnabled = false
        repeatPasswordTextInput.isErrorEnabled = false
    }

    private fun getEditTextByField(field: SignUpField): EditText {
        return when (field) {
            SignUpField.EMAIL -> binding.emailEditText
            SignUpField.USERNAME -> binding.usernameEditText
            SignUpField.PASSWORD -> binding.passwordEditText
            SignUpField.REPEAT_PASSWORD -> binding.repeatPasswordEditText
        }
    }

    private fun getTextInputByField(field: SignUpField): TextInputLayout {
        return when (field) {
            SignUpField.EMAIL -> binding.emailTextInput
            SignUpField.USERNAME -> binding.usernameTextInput
            SignUpField.PASSWORD -> binding.passwordTextInput
            SignUpField.REPEAT_PASSWORD -> binding.repeatPasswordTextInput
        }
    }

}
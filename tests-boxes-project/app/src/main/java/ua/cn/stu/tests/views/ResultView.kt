package ua.cn.stu.tests.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import ua.cn.stu.tests.domain.AuthException
import ua.cn.stu.tests.domain.BackendException
import ua.cn.stu.tests.domain.ConnectionException
import ua.cn.stu.tests.domain.Pending
import ua.cn.stu.tests.domain.Empty
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.domain.Error
import ua.cn.stu.tests.domain.Result
import ua.cn.stu.tests.R
import ua.cn.stu.tests.databinding.PartResultViewBinding
import ua.cn.stu.tests.presentation.base.BaseFragment

/**
 * Display progress-bar for [Pending] result, error message and try again button
 * for [Error] result and nothing else for [Empty] and [Success] results
 */
class ResultView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
)
: ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: PartResultViewBinding
    private var tryAgainAction: (() -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_result_view, this, true)
        binding = PartResultViewBinding.bind(this)
    }

    /**
     * Assign an action for 'Try Again' button.
     */
    fun setTryAgainAction(action: () -> Unit) {
        this.tryAgainAction = action
    }

    /**
     * Set the current result to be displayed to the user.
     */
    fun <T> setResult(fragment: BaseFragment, result: Result<T>) {
        binding.messageTextView.isVisible = result is Error<*>
        binding.errorButton.isVisible = result is Error<*>
        binding.progressBar.isVisible = result is Pending<*>
        if (result is Error) {
            Log.e(javaClass.simpleName, "Error", result.error)
            val message = when (result.error) {
                is ConnectionException -> context.getString(R.string.connection_error)
                is AuthException -> context.getString(R.string.auth_error)
                is BackendException -> result.error.message
                else -> context.getString(R.string.internal_error)
            }
            binding.messageTextView.text = message
            if (result.error is AuthException) {
                renderLogoutButton(fragment)
            } else {
                renderTryAgainButton()
            }
        }
    }

    private fun renderLogoutButton(fragment: BaseFragment) {
        binding.errorButton.setOnClickListener {
            fragment.logout()
        }
        binding.errorButton.setText(R.string.action_logout)
    }

    private fun renderTryAgainButton() {
        binding.errorButton.setOnClickListener { tryAgainAction?.invoke() }
        binding.errorButton.setText(R.string.action_try_again)
    }

}
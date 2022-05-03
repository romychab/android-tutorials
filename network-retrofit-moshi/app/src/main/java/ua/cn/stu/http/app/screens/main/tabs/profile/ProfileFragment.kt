package ua.cn.stu.http.app.screens.main.tabs.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import ua.cn.stu.http.app.model.accounts.entities.Account
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.databinding.FragmentProfileBinding
import ua.cn.stu.http.app.screens.base.BaseFragment
import ua.cn.stu.http.app.utils.findTopNavController
import ua.cn.stu.http.app.utils.observeResults
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    override val viewModel by viewModels<ProfileViewModel>()

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.editProfileButton.setOnClickListener { onEditProfileButtonPressed() }
        binding.logoutButton.setOnClickListener { logout() }
        binding.resultView.setTryAgainAction { viewModel.reload() }

        observeAccountDetails()
    }

    private fun observeAccountDetails() {
        val formatter = SimpleDateFormat.getDateTimeInstance()
        viewModel.account.observeResults(this, binding.root, binding.resultView) { account ->
            binding.emailTextView.text = account.email
            binding.usernameTextView.text = account.username
            binding.createdAtTextView.text = if (account.createdAt == Account.UNKNOWN_CREATED_AT)
                getString(R.string.placeholder)
            else
                formatter.format(Date(account.createdAt))
        }
    }

    private fun onEditProfileButtonPressed() {
        findTopNavController().navigate(R.id.editProfileFragment)
    }

}
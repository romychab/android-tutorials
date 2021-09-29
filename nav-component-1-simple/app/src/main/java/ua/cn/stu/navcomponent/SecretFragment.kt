package ua.cn.stu.navcomponent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.cn.stu.navcomponent.databinding.FragmentSecretBinding

class SecretFragment : Fragment(R.layout.fragment_secret) {

    private lateinit var binding: FragmentSecretBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecretBinding.bind(view)
        binding.closeBoxButton.setOnClickListener {
            // go back to the specified destination
            findNavController().popBackStack(R.id.rootFragment, false)
        }
        binding.goBackButton.setOnClickListener {
            // go back to the previous screen
            findNavController().popBackStack()
        }
    }

}
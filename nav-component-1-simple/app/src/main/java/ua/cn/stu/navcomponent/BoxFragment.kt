package ua.cn.stu.navcomponent

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.cn.stu.navcomponent.databinding.FragmentBoxBinding
import kotlin.random.Random

/**
 * The second fragment launched from the [RootFragment].
 * Arguments: color (int) which should be specified by the argument key [ARG_COLOR].
 *
 * May return result via Fragment Result API with request code = [REQUEST_CODE] and bundle
 * containing random generated number in [EXTRA_RANDOM_NUMBER] (int).
 */
class BoxFragment : Fragment(R.layout.fragment_box){

    private lateinit var binding: FragmentBoxBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoxBinding.bind(view)

        // arguments are located in requireArgument() bundle as usual
        val color = requireArguments().getInt(ARG_COLOR)

        binding.root.setBackgroundColor(color)

        binding.goBackButton.setOnClickListener {
            // just go back to the previous screen, also navigateUp() can be used
            findNavController().popBackStack()
        }
        binding.openSecretButton.setOnClickListener {
            // launch the next screen without any arguments
            findNavController().navigate(R.id.action_boxFragment_to_secretFragment)
        }
        binding.generateNumberButton.setOnClickListener {
            val number = Random.nextInt(100)
            // send the result
            parentFragmentManager.setFragmentResult(REQUEST_CODE, bundleOf(EXTRA_RANDOM_NUMBER to number))
            // go back to the previous screen, also navigateUp() can be used
            findNavController().popBackStack()
        }
    }

    companion object {
        const val ARG_COLOR = "color"

        const val REQUEST_CODE = "RANDOM_NUMBER_REQUEST_CODE"
        const val EXTRA_RANDOM_NUMBER = "EXTRA_RANDOM_NUMBER"
    }
}
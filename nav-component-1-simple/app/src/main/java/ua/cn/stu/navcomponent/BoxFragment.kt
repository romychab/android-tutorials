package ua.cn.stu.navcomponent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.cn.stu.navcomponent.BoxFragment.Companion.EXTRA_RANDOM_NUMBER
import ua.cn.stu.navcomponent.databinding.FragmentBoxBinding
import kotlin.random.Random

/**
 * The second fragment launched from the [RootFragment].
 * Arguments: color (int) and colorName (string).
 *
 * May return result with [EXTRA_RANDOM_NUMBER] key (int).
 */
class BoxFragment : Fragment(R.layout.fragment_box){

    private lateinit var binding: FragmentBoxBinding

    private val args: BoxFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoxBinding.bind(view)

        val color = args.color

        binding.root.setBackgroundColor(color)

        binding.goBackButton.setOnClickListener {
            // just go back to the previous screen, also navigateUp() can be used
            findNavController().popBackStack()
        }
        binding.openSecretButton.setOnClickListener {
            // launch the next screen without any arguments
            findNavController().navigate(BoxFragmentDirections.actionBoxFragmentToSecretFragment())
        }
        binding.generateNumberButton.setOnClickListener {
            val number = Random.nextInt(100)
            // send the result
            publishResults(EXTRA_RANDOM_NUMBER, number)
            // go back to the previous screen, also navigateUp() can be used
            findNavController().popBackStack()
        }
    }

    companion object {
        const val EXTRA_RANDOM_NUMBER = "EXTRA_RANDOM_NUMBER"
    }
}
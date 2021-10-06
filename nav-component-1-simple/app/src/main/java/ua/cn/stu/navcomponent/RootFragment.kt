package ua.cn.stu.navcomponent

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import ua.cn.stu.navcomponent.databinding.FragmentRootBinding

/**
 * The root screen. Can launch [BoxFragment] passing background color and color's name as arguments.
 */
class RootFragment : Fragment(R.layout.fragment_root) {

    private lateinit var binding: FragmentRootBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRootBinding.bind(view)
        binding.openYellowBoxButton.setOnClickListener {
            openBox(Color.rgb(255, 255, 200), getString(R.string.yellow))
        }
        binding.openGreenBoxButton.setOnClickListener {
            openBox(Color.rgb(200, 255, 200), getString(R.string.green))
        }

        // listening for the results from BoxFragment
        listenResults<Int>(BoxFragment.EXTRA_RANDOM_NUMBER) { randomNumber ->
            Toast.makeText(
                requireContext(),
                getString(R.string.generated_number, randomNumber),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openBox(color: Int, colorName: String) {

        // launch BoxFragment with arguments and additional options

        val direction = RootFragmentDirections.actionRootFragmentToBoxFragment(color, colorName)

        findNavController().navigate(
            direction,
            // optional additional options, example of simple animation:
            navOptions {
                anim {
                    enter = R.anim.enter
                    exit = R.anim.exit
                    popEnter = R.anim.pop_enter
                    popExit = R.anim.pop_exit
                }
            }
        )
    }

}
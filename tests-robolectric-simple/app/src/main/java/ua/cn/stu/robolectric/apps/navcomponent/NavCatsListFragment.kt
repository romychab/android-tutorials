package ua.cn.stu.robolectric.apps.navcomponent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.robolectric.CatsAdapterListener
import ua.cn.stu.robolectric.R
import ua.cn.stu.robolectric.catsAdapter
import ua.cn.stu.robolectric.databinding.FragmentCatsBinding
import ua.cn.stu.robolectric.viewmodel.CatListItem
import ua.cn.stu.robolectric.viewmodel.CatsViewModel

@AndroidEntryPoint
class NavCatsListFragment : Fragment(R.layout.fragment_cats),
        CatsAdapterListener {

    private val viewModel by viewModels<CatsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCatsBinding.bind(view)

        val adapter = catsAdapter(this)
        binding.catsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        (binding.catsRecyclerView.itemAnimator as? DefaultItemAnimator)
            ?.supportsChangeAnimations = false
        binding.catsRecyclerView.adapter = adapter
        viewModel.catsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCatDelete(cat: CatListItem.Cat) {
        viewModel.deleteCat(cat)
    }

    override fun onCatToggleFavorite(cat: CatListItem.Cat) {
        viewModel.toggleFavorite(cat)
    }

    override fun onCatChosen(cat: CatListItem.Cat) {
        val direction = NavCatsListFragmentDirections
            .actionNavCatsListFragmentToNavCatDetailsFragment(cat.id)
        findNavController().navigate(direction)
    }

}
package ua.cn.stu.espresso.apps.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.espresso.CatsAdapterListener
import ua.cn.stu.espresso.R
import ua.cn.stu.espresso.catsAdapter
import ua.cn.stu.espresso.databinding.FragmentCatsBinding
import ua.cn.stu.espresso.viewmodel.CatListItem
import ua.cn.stu.espresso.viewmodel.CatsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CatsListFragment : Fragment(R.layout.fragment_cats), CatsAdapterListener,
        HasTitle {

    @Inject
    lateinit var router: FragmentRouter

    private val viewModel by viewModels<CatsViewModel>()

    override val title: String
        get() = getString(R.string.fragment_cats_title)

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
        router.showDetails(cat.id)
    }


}
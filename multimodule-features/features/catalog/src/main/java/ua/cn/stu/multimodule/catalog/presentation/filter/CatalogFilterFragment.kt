package ua.cn.stu.multimodule.catalog.presentation.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.elveum.elementadapter.ElementListAdapter
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.multimodule.catalog.R
import ua.cn.stu.multimodule.catalog.databinding.FragmentFilterBinding
import ua.cn.stu.multimodule.catalog.domain.entities.ProductFilter
import ua.cn.stu.multimodule.core.presentation.BaseScreen
import ua.cn.stu.multimodule.core.presentation.args
import ua.cn.stu.multimodule.core.presentation.viewBinding
import ua.cn.stu.multimodule.core.presentation.viewModelCreator
import ua.cn.stu.multimodule.core.presentation.views.observe
import ua.cn.stu.multimodule.core.presentation.views.setupSimpleList
import javax.inject.Inject

@AndroidEntryPoint
class CatalogFilterFragment : Fragment(R.layout.fragment_filter) {

    class Screen(
        val filter: ProductFilter
    ) : BaseScreen

    private val binding by viewBinding<FragmentFilterBinding>()

    @Inject lateinit var factory: CatalogFilterViewModel.Factory
    private val viewModel by viewModelCreator { factory.create(args()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = createFilterAdapter(
            onApply = {
                viewModel.applyFilter()
            }
        )
        with(binding) {
            filterRecyclerView.setupSimpleList()
            filterRecyclerView.adapter = adapter
            observeState(adapter)
            setupListeners()
        }
        viewModel.initBackListener(viewLifecycleOwner.lifecycleScope)
    }

    private fun FragmentFilterBinding.observeState(adapter: ElementListAdapter<FilterItem>) {
        root.observe(viewLifecycleOwner, viewModel.stateLiveValue) { filterItems ->
            adapter.submitList(filterItems)
        }
    }

    private fun FragmentFilterBinding.setupListeners() {
        root.setTryAgainListener { viewModel.load() }
    }

}
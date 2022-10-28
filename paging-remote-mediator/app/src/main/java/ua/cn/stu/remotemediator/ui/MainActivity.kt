package ua.cn.stu.remotemediator.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elveum.elementadapter.context
import com.elveum.elementadapter.setTintColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.cn.stu.remotemediator.R
import ua.cn.stu.remotemediator.databinding.ActivityMainBinding
import ua.cn.stu.remotemediator.databinding.ItemLaunchBinding
import ua.cn.stu.remotemediator.ui.base.*

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val adapter by lazy {
        launchesAdapter(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupList()
        setupRefreshLayout()
        setupYearsSpinner()

        observeLaunches()
        observeState()
        observeToasts()

        handleListVisibility()
        handleScrollingToTop()
    }

    private fun setupYearsSpinner() {
        val items = (listOf(null) + (2006..2025))
            .map { Year(this, it) }
            .toList()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            items
        )
        binding.yearSpinner.adapter = adapter
        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val year = adapterView.adapter.getItem(pos) as Year
                viewModel.year = year.value
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        val currentYearIndex = items.indexOfFirst { it.value == viewModel.year }
        binding.yearSpinner.setSelection(currentYearIndex)
    }

    private fun setupList() {
        binding.launchesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.launchesRecyclerView.adapter = adapter
        (binding.launchesRecyclerView.itemAnimator as? DefaultItemAnimator)
            ?.supportsChangeAnimations = false
        binding.launchesRecyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        lifecycleScope.launch {
            waitForLoad()
            val tryAgainAction: TryAgainAction = { adapter.retry() }
            val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)
            val adapterWithLoadState =
                adapter.withLoadStateFooter(footerAdapter)
            binding.launchesRecyclerView.adapter = adapterWithLoadState
        }
    }

    private fun setupRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun observeLaunches() {
        lifecycleScope.launch {
            viewModel.launchesListFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun observeState() {
        val loadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadingState,
            binding.swipeRefreshLayout
        ) {
            adapter.refresh()
        }
        adapter.loadStateFlow
            .debounce(300)
            .onEach {
                loadStateHolder.bind(it.refresh)
            }
            .launchIn(lifecycleScope)
    }

    private fun handleListVisibility() = lifecycleScope.launch {
        // list should be hidden if an error is displayed OR if items are being loaded after the error:
        // (current state = Error) OR (prev state = Error)
        //   OR
        // (before prev state = Error, prev state = NotLoading, current state = Loading)
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.launchesRecyclerView.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error
                            && previous is LoadState.NotLoading
                            && current is LoadState.Loading)
            }
    }

    private fun handleScrollingToTop() = lifecycleScope.launch {
        // list should be scrolled to the 1st item (index = 0) if data has been reloaded:
        // (prev state = Loading, current state = NotLoading)
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 2)
            .collect { (previousState, currentState) ->
                if (previousState is LoadState.Loading
                        && currentState is LoadState.NotLoading) {
                    delay(200)
                    binding.launchesRecyclerView.scrollToPosition(0)
                }
            }
    }

    private fun getRefreshLoadStateFlow(adapter: PagingDataAdapter<*, *>): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    private suspend fun waitForLoad() {
        adapter.onPagesUpdatedFlow
            .map { adapter.itemCount }
            .first { it > 0 }
    }

    private fun observeToasts() {
        viewModel.toastEvent.observeEvent(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

}
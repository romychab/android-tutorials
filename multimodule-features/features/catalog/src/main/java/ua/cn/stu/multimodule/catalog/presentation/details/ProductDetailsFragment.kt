package ua.cn.stu.multimodule.catalog.presentation.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.multimodule.catalog.R
import ua.cn.stu.multimodule.catalog.databinding.FragmentProductDetailsBinding
import ua.cn.stu.multimodule.core.presentation.*
import ua.cn.stu.multimodule.core.presentation.views.observe
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    class Screen(
        val productId: Long,
    ) : BaseScreen

    @Inject
    lateinit var factory: ProductDetailsViewModel.Factory
    private val viewModel by viewModelCreator { factory.create(args()) }

    private val binding by viewBinding<FragmentProductDetailsBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupListeners()
            observeState()
        }
    }

    private fun FragmentProductDetailsBinding.observeState() {
        root.observe(viewLifecycleOwner, viewModel.stateLiveValue) { state ->
            val product = state.product
            productImageView.loadUrl(product.photo)
            productTitleTextView.text = product.name
            detailsTextView.text = product.details
            addToCartButton.isEnabled = state.enableAddToCartButton
            addToCartButton.isInvisible = !state.showAddToCartButton
            addToCartButton.setText(state.addToCartTextRes)
            addToCartProgressBar.isInvisible = !state.showAddToCartProgress
        }
    }

    private fun FragmentProductDetailsBinding.setupListeners() {
        root.setTryAgainListener { viewModel.reload() }
        addToCartButton.setOnClickListener { viewModel.addToCart() }
    }

}
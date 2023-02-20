package ua.cn.stu.multimodule.cart.presentation.cartlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.elveum.elementadapter.ElementListAdapter
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.multimodule.cart.R
import ua.cn.stu.multimodule.cart.databinding.FragmentCartListBinding
import ua.cn.stu.multimodule.cart.presentation.cartlist.entities.UiCartItem
import ua.cn.stu.multimodule.core.presentation.viewBinding
import ua.cn.stu.multimodule.core.presentation.views.observe
import ua.cn.stu.multimodule.core.presentation.views.setupSimpleList

@AndroidEntryPoint
class CartListFragment : Fragment(R.layout.fragment_cart_list) {

    private val viewModel by viewModels<CartListViewModel>()

    private val binding by viewBinding<FragmentCartListBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = createCartAdapter(viewModel)
        with(binding) {
            setupList(adapter)
            setupActions()
            observeState(adapter)
            setupListeners()
        }
        viewModel.initBackListener(viewLifecycleOwner.lifecycleScope)
    }

    private fun FragmentCartListBinding.setupList(adapter: ElementListAdapter<UiCartItem>) {
        cartRecyclerView.setupSimpleList()
        cartRecyclerView.adapter = adapter
    }

    private fun FragmentCartListBinding.setupActions() {
        deleteAction.actionImageView.setImageResource(R.drawable.ic_delete)
        deleteAction.actionTextView.setText(R.string.cart_action_delete)

        showDetailsAction.actionImageView.setImageResource(R.drawable.ic_details)
        showDetailsAction.actionTextView.setText(R.string.cart_action_details)

        editQuantityAction.actionImageView.setImageResource(R.drawable.ic_edit)
        editQuantityAction.actionTextView.setText(R.string.cart_action_edit)
    }

    private fun FragmentCartListBinding.observeState(adapter: ElementListAdapter<UiCartItem>) {
        root.observe(viewLifecycleOwner, viewModel.stateLiveValue) { state ->
            adapter.submitList(state.cartItems)
            totalPriceValueTextView.text = state.totalPrice.text
            if (state.totalPriceWithDiscount == state.totalPrice) {
                totalPriceWithDiscountValueTextView.isVisible = false
                totalPriceValueTextView.background = null
            } else {
                totalPriceWithDiscountValueTextView.isVisible = true
                totalPriceWithDiscountValueTextView.text = state.totalPriceWithDiscount.text
                totalPriceValueTextView.setBackgroundResource(ua.cn.stu.multimodule.theme.R.drawable.core_theme_diagonal_line)
            }
            binding.createOrderButton.isEnabled = state.enableCreateOrderButton
            binding.deleteAction.root.isVisible = state.showDeleteAction
            binding.editQuantityAction.root.isVisible = state.showEditQuantityAction
            binding.showDetailsAction.root.isVisible = state.showDetailsAction
            binding.actionsContainer.isVisible = state.showActionsPanel
        }
    }

    private fun FragmentCartListBinding.setupListeners() {
        createOrderButton.setOnClickListener {
            viewModel.createOrder()
        }
        deleteAction.root.setOnClickListener {
            viewModel.deleteSelected()
        }
        editQuantityAction.root.setOnClickListener {
            viewModel.showEditQuantity()
        }
        showDetailsAction.root.setOnClickListener {
            viewModel.showDetails()
        }
        root.setTryAgainListener { viewModel.reload() }
    }

}
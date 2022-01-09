package ua.cn.stu.room.screens.main.tabs.admin

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.cn.stu.room.R
import ua.cn.stu.room.databinding.ItemTreeElementBinding

class AdminItemsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<AdminItemsAdapter.Holder>(), View.OnClickListener {

    private var items: List<AdminTreeItem> = emptyList()

    override fun onClick(v: View) {
        val item = v.tag as AdminTreeItem
        listener.onItemToggled(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTreeElementBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.itemView.tag = item
        with(holder.binding) {
            attributesTextView.text = prepareAttributesText(item)
            expandCollapseIndicatorImageView.setImageResource(getExpansionIcon(item))
            adjustStartOffset(item, attributesTextView)
            holder.binding.root.isClickable = item.expansionStatus != ExpansionStatus.NOT_EXPANDABLE
        }
    }

    override fun getItemCount(): Int = items.size

    fun renderItems(items: List<AdminTreeItem>) {
        val diffCallback = AdminTreeItemDiffCallback(this.items, items)
        val result = DiffUtil.calculateDiff(diffCallback)
        this.items = items
        result.dispatchUpdatesTo(this)
    }

    private fun prepareAttributesText(item: AdminTreeItem): CharSequence {
        // just a bit of HTML for easier implementation.
        // please note it may work bad on devices with small screens
        val attributesText = item.attributes.entries.joinToString("<br>") {
            if (it.value.isNotBlank()) {
                "<b>${it.key}</b>: ${it.value}"
            } else {
                "<b>${it.key}</b>"
            }
        }
        return Html.fromHtml(attributesText)
    }

    @DrawableRes
    private fun getExpansionIcon(item: AdminTreeItem): Int {
        return when (item.expansionStatus) {
            ExpansionStatus.EXPANDED -> R.drawable.ic_minus
            ExpansionStatus.COLLAPSED -> R.drawable.ic_plus
            else -> R.drawable.ic_dot
        }
    }

    private fun adjustStartOffset(item: AdminTreeItem, attributesTextView: TextView) {
        val context = attributesTextView.context
        val spacePerLevel = context.resources.getDimensionPixelSize(R.dimen.tree_level_size)
        val totalSpace = (item.level + 1) * spacePerLevel

        val layoutParams = attributesTextView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = totalSpace
        attributesTextView.layoutParams = layoutParams
    }

    interface Listener {
        /**
         * Called when user toggles the specified item.
         */
        fun onItemToggled(item: AdminTreeItem)
    }

    class Holder(val binding: ItemTreeElementBinding) : RecyclerView.ViewHolder(binding.root)
}
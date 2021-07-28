package ua.cn.stu.simplemvvm.views.changecolor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.cn.stu.simplemvvm.databinding.ItemColorBinding
import ua.cn.stu.simplemvvm.model.colors.NamedColor

/**
 * Adapter for displaying the list of available colors
 * @param listener callback which notifies about user actions on items in the list, see [Listener] for details.
 */
class ColorsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<ColorsAdapter.Holder>(), View.OnClickListener {

    var items: List<NamedColorListItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val item = v.tag as NamedColor
        listener.onColorChosen(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemColorBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val namedColor = items[position].namedColor
        val selected = items[position].selected
        with(holder.binding) {
            root.tag = namedColor
            colorNameTextView.text = namedColor.name
            colorView.setBackgroundColor(namedColor.value)
            selectedIndicatorImageView.visibility = if (selected) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    class Holder(
        val binding: ItemColorBinding
    ) : RecyclerView.ViewHolder(binding.root)


    interface Listener {
        /**
         * Called when user chooses the specified color
         * @param namedColor color chosen by the user
         */
        fun onColorChosen(namedColor: NamedColor)
    }

}
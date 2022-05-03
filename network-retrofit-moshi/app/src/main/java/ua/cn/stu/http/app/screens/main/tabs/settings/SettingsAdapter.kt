package ua.cn.stu.http.app.screens.main.tabs.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.cn.stu.http.app.model.boxes.entities.Box
import ua.cn.stu.http.app.model.boxes.entities.BoxAndSettings
import ua.cn.stu.http.app.R

class SettingsAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<SettingsAdapter.Holder>(), View.OnClickListener {

    private var settings: List<BoxAndSettings> = emptyList()

    override fun onClick(v: View?) {
        val checkBox = v as CheckBox
        val box = v.tag as Box
        if (checkBox.isChecked) {
            listener.enableBox(box)
        } else {
            listener.disableBox(box)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val checkBox = inflater.inflate(R.layout.item_setting, parent, false) as CheckBox
        checkBox.setOnClickListener(this)
        return Holder(checkBox)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val setting = settings[position]
        val context = holder.itemView.context
        holder.checkBox.tag = setting.box

        if (holder.checkBox.isChecked != setting.isActive) {
            holder.checkBox.isChecked = setting.isActive
        }

        val colorName = setting.box.colorName
        holder.checkBox.text = context.getString(R.string.enable_checkbox, colorName)
    }

    override fun getItemCount(): Int = settings.size

    fun renderSettings(settings: List<BoxAndSettings>) {
        val diffResult = DiffUtil.calculateDiff(BoxSettingsDiffCallback(this.settings, settings))
        this.settings = settings
        diffResult.dispatchUpdatesTo(this)
    }

    class Holder(val checkBox: CheckBox) : RecyclerView.ViewHolder(checkBox)

    interface Listener {
        fun enableBox(box: Box)
        fun disableBox(box: Box)
    }
}
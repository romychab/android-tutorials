package ua.cn.stu.remotemediator.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.cn.stu.remotemediator.R
import ua.cn.stu.remotemediator.databinding.ItemLaunchBinding
import ua.cn.stu.remotemediator.domain.Launch

class LaunchesAdapter(
    private val listener: Listener
) : PagingDataAdapter<LaunchUiEntity, LaunchesAdapter.Holder>(
    diffCallback = LaunchesItemCallback()
), View.OnClickListener {

    override fun onClick(v: View) {
        val launch = v.tag as LaunchUiEntity
        if (v.id == R.id.checkContainer) {
            listener.onToggleCheckState(launch)
        } else {
            listener.onToggleSuccessFlag(launch)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val launch = getItem(position) ?: return
        with(holder.binding) {
            statusImageView.tag = launch
            checkContainer.tag = launch

            nameTextView.text = launch.name
            descriptionTextView.text = launch.description
            selectCheckBox.isChecked = launch.isChecked
            setBackground(root, launch.isChecked)
            setImage(photoImageView, launch)
            yearValueTextView.text = launch.year.toString()

            if (launch.isSuccess) {
                setStatus(statusImageView, R.drawable.ic_success, R.color.success)
            } else {
                setStatus(statusImageView, R.drawable.ic_fail, R.color.fail)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLaunchBinding
            .inflate(inflater, parent, false)
        binding.checkContainer.setOnClickListener(this)
        binding.statusImageView.setOnClickListener(this)
        return Holder(binding)
    }

    private fun setBackground(root: View, isChecked: Boolean) {
        if (isChecked) {
            root.setBackgroundColor(
                ContextCompat.getColor(root.context, R.color.checked_background)
            )
        } else {
            root.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun setStatus(imageView: ImageView, @DrawableRes drawable: Int, @ColorRes colorRes: Int) {
        val context = imageView.context
        imageView.setImageResource(drawable)
        imageView.imageTintList = ColorStateList
            .valueOf(ContextCompat.getColor(context, colorRes))
    }

    private fun setImage(imageView: ImageView, launch: Launch) {
        val context = imageView.context
        if (launch.imageUrl.isNotBlank()) {
            imageView.imageTintList = null
            Glide.with(context)
                .load(launch.imageUrl)
                .centerInside()
                .into(imageView)
        } else {
            imageView.imageTintList = ColorStateList.valueOf(Color.LTGRAY)
            Glide.with(context)
                .load(R.drawable.ic_no_image)
                .into(imageView)
        }
    }

    class Holder(val binding: ItemLaunchBinding) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun onToggleSuccessFlag(launch: LaunchUiEntity)
        fun onToggleCheckState(launch: LaunchUiEntity)
    }
}
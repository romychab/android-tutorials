package ua.cn.stu.remotemediator.ui

import androidx.recyclerview.widget.DiffUtil

class LaunchesItemCallback : DiffUtil.ItemCallback<LaunchUiEntity>() {

    override fun areItemsTheSame(oldItem: LaunchUiEntity, newItem: LaunchUiEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LaunchUiEntity, newItem: LaunchUiEntity): Boolean {
        return oldItem == newItem
    }

}
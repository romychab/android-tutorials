package ua.cn.stu.paging.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.cn.stu.paging.R
import ua.cn.stu.paging.databinding.ItemUserBinding
import ua.cn.stu.paging.views.UserListItem

/**
 * Adapter for rendering users list in a RecyclerView.
 */
class UsersAdapter(
    private val listener: Listener
) : PagingDataAdapter<UserListItem, UsersAdapter.Holder>(UsersDiffCallback()), View.OnClickListener {

    override fun onClick(v: View) {
        val user = v.tag as UserListItem
        if (v.id == R.id.starImageView) {
            listener.onToggleFavoriteFlag(user)
        } else if (v.id == R.id.deleteImageView) {
            listener.onUserDelete(user)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = getItem(position) ?: return
        with (holder.binding) {
            userNameTextView.text = user.name
            userCompanyTextView.text = user.company

            userProgressBar.alpha = if (user.inProgress) 1f else 0f // life hack xD
            starImageView.isInvisible = user.inProgress
            deleteImageView.isInvisible = user.inProgress

            setIsFavorite(starImageView, user.isFavorite)
            loadUserPhoto(photoImageView, user.imageUrl)

            starImageView.tag = user
            deleteImageView.tag = user
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        binding.starImageView.setOnClickListener(this)
        binding.deleteImageView.setOnClickListener(this)
        return Holder(binding)
    }

    private fun setIsFavorite(starImageView: ImageView, isFavorite: Boolean) {
        val context = starImageView.context
        if (isFavorite) {
            starImageView.setImageResource(R.drawable.ic_star)
            starImageView.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.active)
            )
        } else {
            starImageView.setImageResource(R.drawable.ic_star_outline)
            starImageView.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.inactive)
            )
        }
    }

    private fun loadUserPhoto(imageView: ImageView, url: String) {
        val context = imageView.context
        if (url.isNotBlank()) {
            Glide.with(context)
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .error(R.drawable.ic_user_avatar)
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.ic_user_avatar)
                .into(imageView)
        }
    }

    class Holder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        /**
         * Called when the user taps the "Delete" button in a list item
         */
        fun onUserDelete(userListItem: UserListItem)

        /**
         * Called when the user taps the "Star" button in a list item.
         */
        fun onToggleFavoriteFlag(userListItem: UserListItem)
    }
}

// ---

class UsersDiffCallback : DiffUtil.ItemCallback<UserListItem>() {
    override fun areItemsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean {
        return oldItem == newItem
    }
}

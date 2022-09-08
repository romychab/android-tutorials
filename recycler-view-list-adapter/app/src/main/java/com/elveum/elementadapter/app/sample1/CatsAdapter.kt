package com.elveum.elementadapter.app.sample1

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.elveum.elementadapter.app.R
import com.elveum.elementadapter.app.databinding.ItemCatBinding
import com.elveum.elementadapter.app.model.Cat

class CatsAdapter(
    private val listener: Listener
) : ListAdapter<Cat, CatsAdapter.CatHolder>(ItemCallback), View.OnClickListener {

    override fun onClick(v: View) {
        val cat = v.tag as Cat
        when (v.id) {
            R.id.deleteImageView -> listener.onDeleteCat(cat)
            R.id.favoriteImageView -> listener.onToggleFavorite(cat)
            else -> listener.onChooseCat(cat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCatBinding.inflate(inflater, parent, false)

        binding.deleteImageView.setOnClickListener(this)
        binding.favoriteImageView.setOnClickListener(this)
        binding.root.setOnClickListener(this)

        return CatHolder(binding)
    }

    override fun onBindViewHolder(holder: CatHolder, position: Int) {
        val cat = getItem(position)

        with(holder.binding) {
            root.tag = cat
            deleteImageView.tag = cat
            favoriteImageView.tag = cat

            catNameTextView.text = cat.name
            catDescriptionTextView.text = cat.description
            catImageView.load(cat.photoUrl) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.circle)
            }
            favoriteImageView.setImageResource(
                if (cat.isFavorite)
                    R.drawable.ic_favorite
                else
                    R.drawable.ic_favorite_not
            )
            val tintColor = if (cat.isFavorite)
                R.color.highlighted_action
            else
                R.color.action
            favoriteImageView.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(root.context, tintColor)
            )
        }
    }

    interface Listener {
        fun onChooseCat(cat: Cat)
        fun onToggleFavorite(cat: Cat)
        fun onDeleteCat(cat: Cat)
    }

    class CatHolder(
        val binding: ItemCatBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object ItemCallback : DiffUtil.ItemCallback<Cat>() {
        override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
            return oldItem == newItem
        }
    }
}









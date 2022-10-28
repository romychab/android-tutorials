package ua.cn.stu.robolectric

import coil.load
import coil.transform.CircleCropTransformation
import com.elveum.elementadapter.adapter
import com.elveum.elementadapter.addBinding
import com.elveum.elementadapter.context
import com.elveum.elementadapter.setTintColor
import ua.cn.stu.robolectric.databinding.ItemCatBinding
import ua.cn.stu.robolectric.databinding.ItemHeaderBinding
import ua.cn.stu.robolectric.viewmodel.CatListItem

interface CatsAdapterListener {
    fun onCatDelete(cat: CatListItem.Cat)
    fun onCatToggleFavorite(cat: CatListItem.Cat)
    fun onCatChosen(cat: CatListItem.Cat)
}

fun catsAdapter(listener: CatsAdapterListener) = adapter {
    addBinding<CatListItem.Cat, ItemCatBinding> {
        areItemsSame = { oldCat, newCat -> oldCat.id == newCat.id }
        bind { cat ->
            catNameTextView.text = cat.name
            catDescriptionTextView.text = cat.description
            catImageView.load(cat.photoUrl) {
                transformations(CircleCropTransformation())
                placeholder(R.drawable.circle)
            }
            favoriteImageView.setImageResource(
                if (cat.isFavorite) R.drawable.ic_favorite
                else R.drawable.ic_favorite_not
            )
            favoriteImageView.setTintColor(
                if (cat.isFavorite) R.color.highlighted_action
                else R.color.action
            )
        }
        listeners {
            deleteImageView.onClick { listener.onCatDelete(it) }
            favoriteImageView.onClick { listener.onCatToggleFavorite(it) }
            root.onClick { listener.onCatChosen(it) }
        }
    }
    addBinding<CatListItem.Header, ItemHeaderBinding> {
        areItemsSame = { oldHeader, newHeader -> oldHeader.headerId == newHeader.headerId }
        bind { header ->
            titleTextView.text = context().getString(
                R.string.cats,
                header.fromIndex, header.toIndex
            )
        }
    }
}
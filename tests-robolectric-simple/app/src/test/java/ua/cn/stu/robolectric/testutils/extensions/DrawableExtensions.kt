package ua.cn.stu.robolectric.testutils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import org.robolectric.Shadows
import ua.cn.stu.robolectric.testutils.imageloader.FakeImageLoaderDrawable

fun ImageView.containsDrawable(@DrawableRes res: Int, @ColorRes tintColorRes: Int? = null): Boolean {
    if (!containsTint(tintColorRes)) return false
    return Shadows.shadowOf(drawable).createdFromResId == res
}

fun ImageView.containsDrawable(other: Drawable, @ColorRes tintColorRes: Int? = null): Boolean {
    if (!containsTint(tintColorRes)) return false
    val imageDrawable = drawable
    return if (other is FakeImageLoaderDrawable) {
        imageDrawable is FakeImageLoaderDrawable &&
                imageDrawable.url == other.url
    } else {
        other == imageDrawable
    }
}

private fun ImageView.containsTint(@ColorRes tintColorRes: Int?): Boolean {
    return if (tintColorRes != null) {
        val imageTintList = this.imageTintList
        imageTintList != null && imageTintList.defaultColor == ContextCompat.getColor(context, tintColorRes)
    } else {
        this.imageTintList == null
    }
}
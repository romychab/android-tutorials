package ua.cn.stu.multimodule.core.presentation

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import coil.transform.RoundedCornersTransformation

/**
 * Load the image URL into [ImageView] and round image corners.
 */
fun ImageView.loadUrl(url: String) {
    load(url) {
        transformations(RoundedCornersTransformation(16f))
    }
}

/**
 * Load the image resource into [ImageView] and round image corners.
 */
fun ImageView.loadResource(@DrawableRes id: Int) {
    load(id) {
        transformations(RoundedCornersTransformation(16f))
    }
}


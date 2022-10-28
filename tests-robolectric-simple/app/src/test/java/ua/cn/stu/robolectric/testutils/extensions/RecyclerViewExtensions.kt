package ua.cn.stu.robolectric.testutils.extensions

import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import org.robolectric.Shadows

fun RecyclerView.requireViewHolderAt(position: Int): RecyclerView.ViewHolder {
    scrollToPosition(position)
    Shadows.shadowOf(Looper.getMainLooper()).idle()
    return findViewHolderForAdapterPosition(position)!!
}

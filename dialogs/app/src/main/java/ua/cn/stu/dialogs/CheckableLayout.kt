package ua.cn.stu.dialogs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.constraintlayout.widget.ConstraintLayout

class CheckableLayout(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
) : ConstraintLayout(context, attrs, defStyleAttr), Checkable {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val checkableChild: Checkable by lazy { findCheckableChild(this) }

    override fun isChecked(): Boolean {
        return checkableChild.isChecked
    }

    override fun toggle() {
        checkableChild.toggle()
    }

    override fun setChecked(checked: Boolean) {
        checkableChild.isChecked = checked
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val checkableView = checkableChild as View
        checkableView.isFocusableInTouchMode = false
        checkableView.isFocusable = false
        checkableView.isClickable = false
    }

    private fun findCheckableChild(root: ViewGroup): Checkable {
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is Checkable) return child
            if (child is ViewGroup) return findCheckableChild(child)
        }
        throw IllegalStateException("Can't find checkable child view")
    }

}
package ua.cn.stu.http.app.utils

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ScrollView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import ua.cn.stu.http.app.model.Success
import ua.cn.stu.http.app.model.Result
import ua.cn.stu.http.app.screens.base.BaseFragment
import ua.cn.stu.http.app.views.ResultView

fun <T> LiveData<T>.requireValue(): T {
    return this.value ?: throw IllegalStateException("Value is empty")
}

fun <T> LiveData<Result<T>>.observeResults(fragment: BaseFragment, root: View, resultView: ResultView, onSuccess: (T) -> Unit) {
    observe(fragment.viewLifecycleOwner) { result ->
        resultView.setResult(fragment, result)
        val rootView: View = if (root is ScrollView)
            root.getChildAt(0)
        else
            root

        if (rootView is ViewGroup && rootView !is RecyclerView && root !is AbsListView) {
            rootView.children
                .filter { it != resultView }
                .forEach {
                    it.isVisible = result is Success<*>
                }
        }
        if (result is Success) onSuccess.invoke(result.value)
    }
}


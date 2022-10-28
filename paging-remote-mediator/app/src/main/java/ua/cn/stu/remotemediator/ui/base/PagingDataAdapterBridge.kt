package ua.cn.stu.remotemediator.ui.base

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.viewbinding.ViewBinding
import com.elveum.elementadapter.delegate.AdapterDelegate
import com.elveum.elementadapter.delegate.adapterDelegate
import com.elveum.elementadapter.delegate.simpleAdapterDelegate
import com.elveum.elementadapter.dsl.AdapterScope
import com.elveum.elementadapter.dsl.BindingHolder
import com.elveum.elementadapter.dsl.ConcreteItemTypeScope

/* Example of adapter for more than 1 item type
fun <T : Any> pagingAdapter(
    block: AdapterScope<T>.() -> Unit
): PagingDataAdapter<T, BindingHolder> {
    val delegate = adapterDelegate(block)
    return PagingDataAdapterBridge(delegate)
}
*/


inline fun <reified T : Any, reified B : ViewBinding> pagingAdapter(
    noinline block: ConcreteItemTypeScope<T, B>.() -> Unit
): PagingDataAdapter<T, BindingHolder> {
    val delegate = simpleAdapterDelegate(block)
    return PagingDataAdapterBridge(delegate)
}


class PagingDataAdapterBridge<T : Any>(
    private val adapterDelegate: AdapterDelegate<T>
) : PagingDataAdapter<T, BindingHolder>(
    adapterDelegate.itemCallback()
) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return 0
        return adapterDelegate.getItemViewType(item)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val item = getItem(position) ?: return
        adapterDelegate.onBindViewHolder(holder, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return adapterDelegate.onCreateViewHolder(parent, viewType)
    }

}
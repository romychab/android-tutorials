package ua.cn.stu.multimodule.core.presentation

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KProperty

/**
 * Create and hold a [ViewBinding] class only while fragment view exists and is not
 * destroyed (after onCreateView and before onDestroyView callbacks).
 *
 * Usage example:
 *
 * ```
 * class MyFragment : Fragment(R.layout.fragment_test) {
 *
 *     private val binding by viewBinding<FragmentTestBinding>()
 *
 *     fun doSomething() {
 *         binding.textView.text = "Oooops"
 *     }
 * }
 * ```
 */
inline fun <reified B : ViewBinding> Fragment.viewBinding(): ViewBindingDelegate<B> {
    val fragment = this
    return ViewBindingDelegate(fragment, B::class.java)
}

class ViewBindingDelegate<B : ViewBinding>(
    private val fragment: Fragment,
    private val viewBindingClass: Class<B>
) {

    var binding: B? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): B {
        val viewLifecycleOwner = fragment.viewLifecycleOwner
        if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            throw IllegalStateException("Called after onDestroyView()")
        } else if (fragment.view != null) {
            return getOrCreateBinding(viewLifecycleOwner)
        } else {
            throw IllegalStateException("Called before onViewCreated()")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getOrCreateBinding(lifecycleOwner: LifecycleOwner): B {
        return this.binding ?: let {
            val method = viewBindingClass.getMethod("bind", View::class.java)
            val binding = method.invoke(null, fragment.view) as B

            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    this@ViewBindingDelegate.binding = null
                }
            })

            this.binding = binding
            binding
        }
    }
}
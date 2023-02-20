package ua.cn.stu.multimodule.navigation

import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import ua.cn.stu.multimodule.core.impl.ActivityRequired
import ua.cn.stu.multimodule.navigation.presentation.navigation.NavComponentRouter
import ua.cn.stu.multimodule.navigation.presentation.navigation.RouterHolder
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This router is a wrapper for [NavComponentRouter] which can be
 * safely used out of the activity lifecycle scope.
 */
@Singleton
class GlobalNavComponentRouter @Inject constructor(
    private val destinationsProvider: DestinationsProvider,
) : ActivityRequired {

    private var activity: FragmentActivity? = null
    private var started = false
    private var completelyDestroyed = true
    private val commands = mutableListOf<() -> Unit>()

    private val onBackPressHandlers = LinkedHashSet<() -> Boolean>()

    override fun onCreated(activity: FragmentActivity) {
        this.completelyDestroyed = false
        this.activity = activity
        setupBackHandlers()
    }

    override fun onStarted() {
        started = true
        commands.forEach { it() }
        commands.clear()
    }

    override fun onStopped() {
        started = false
    }

    override fun onDestroyed() {
        if (activity?.isFinishing == true) {
            commands.clear()
            completelyDestroyed = true
        }
    }

    /**
     * Register back handler.
     * @param scope should be viewLifecycleScope or dialog lifecycle scope
     * @param handler callback which should return true if you want to cancel default back logic
     */
    fun registerBackHandler(scope: CoroutineScope, handler: () -> Boolean) {
        scope.launch {
            suspendCancellableCoroutine { continuation ->
                onBackPressHandlers.add(handler)
                continuation.invokeOnCancellation {
                    onBackPressHandlers.remove(handler)
                }
            }
        }
    }

    /**
     * Navigate back to the previous screen.
     */
    fun pop() = invoke {
        requireRealRouter().pop()
    }

    /**
     * Close all screens and start an initial root screen provided by [DestinationsProvider].
     */
    fun restart() = invoke {
        requireRealRouter().switchToStack(destinationsProvider.provideStartDestinationId())
    }

    /**
     * Launch the specified destination.
     */
    fun launch(@IdRes destinationId: Int, args: java.io.Serializable? = null) = invoke {
        requireRealRouter().launch(destinationId, args)
    }

    /**
     * Close all screens and launch tabs screen provided by [DestinationsProvider].
     * @param startTabDestinationId initial tab to be opened.
     */
    fun startTabs(startTabDestinationId: Int? = null) = invoke {
        requireRealRouter().switchToTabs(destinationsProvider.provideMainTabs(), startTabDestinationId)
    }

    private fun setupBackHandlers() {
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (requireRealRouter().isDialog()) {
                    processAsUsual()
                    return
                }
                onBackPressHandlers.reversed().forEach { handler ->
                    if (handler.invoke()) {
                        return
                    }
                }
                processAsUsual()
            }

            private fun processAsUsual() {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        })
    }

    private fun invoke(command: () -> Unit) {
        if (started) {
            command()
        } else if (!completelyDestroyed) {
            commands.add(command)
        }
    }

    private fun requireRealRouter(): NavComponentRouter {
        return (activity as RouterHolder).requireRouter()
    }

    private fun requireActivity(): FragmentActivity {
        return activity!!
    }
}
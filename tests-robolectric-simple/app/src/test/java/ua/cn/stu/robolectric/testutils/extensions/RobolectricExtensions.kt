package ua.cn.stu.robolectric.testutils.extensions

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import org.robolectric.android.controller.ActivityController

fun <T : Activity> ActivityController<T>.require(): T {
    return get()!!
}

fun <T : Activity> ActivityScenario<T>.with(block: T.() -> Unit) {
    onActivity(block)
}
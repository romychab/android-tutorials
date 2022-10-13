package ua.cn.stu.espresso.apps.testutils

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import ua.cn.stu.espresso.apps.testutils.fragments.TestFragmentActivity

/**
 * Launch a fragment managed by Hilt in the test activity container.
 *
 * @see TestFragmentActivity
 */
inline fun <reified T : Fragment> launchHiltFragment(
    noinline creator: (() -> T)? = null
): AutoCloseable {
    val intent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            TestFragmentActivity::class.java
        )
    )

    return ActivityScenario.launch<TestFragmentActivity>(intent).onActivity {
        val fragment = creator?.invoke() ?: it.supportFragmentManager.fragmentFactory.instantiate(
            T::class.java.classLoader!!,
            T::class.java.name
        )
        it.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment)
            .commitNow()
    }
}
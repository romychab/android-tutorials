package ua.cn.stu.espresso.apps.testutils.espresso

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Check that the RecyclerView contains exactly the specified
 * count of items.
 */
fun withItemsCount(count: Int): Matcher<View> = WithItemsCount(count)

private class WithItemsCount(
    private val count: Int
) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("recyclerView with item count = $count")
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is RecyclerView) return false
        val adapter = item.adapter ?: return false
        return adapter.itemCount == count
    }

}
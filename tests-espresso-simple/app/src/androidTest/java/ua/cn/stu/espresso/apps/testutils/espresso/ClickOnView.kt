package ua.cn.stu.espresso.apps.testutils.espresso

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

/**
 * Click on the view with the specified [id].
 * This method is useful for clicking on views inside list items.
 */
fun clickOnView(@IdRes id: Int? = null): ViewAction = ClickOnView(id)

private class ClickOnView(private val id: Int? = null) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isDisplayingAtLeast(90)
    }

    override fun getDescription(): String {
        return "Click on item view"
    }

    override fun perform(uiController: UiController?, view: View) {
        if (id == null) {
            view.performClick()
        } else {
            view.findViewById<View>(id)?.performClick()
        }
    }

}
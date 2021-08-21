package ua.cn.stu.foundation.views.activity

/**
 * If you don't want to use [BaseActivity] for some reason (for example you have 2 or more activity
 * hierarchies then you can use this holder instead.
 * Please note that you need to call methods of [delegate] manually from your activity in this case.
 * See [ActivityDelegate] for details.
 */
interface ActivityDelegateHolder {

    val delegate: ActivityDelegate

}
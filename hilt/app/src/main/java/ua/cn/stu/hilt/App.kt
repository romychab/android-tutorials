package ua.cn.stu.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Entry point of the app should be annotated with [HiltAndroidApp].
 */
@HiltAndroidApp
class App : Application()
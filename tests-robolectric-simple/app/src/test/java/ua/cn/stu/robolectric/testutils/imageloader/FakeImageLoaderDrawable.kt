package ua.cn.stu.robolectric.testutils.imageloader

import android.graphics.Color
import android.graphics.drawable.ColorDrawable

class FakeImageLoaderDrawable(
    val url: String
) : ColorDrawable(Color.BLACK)
package ua.cn.stu.robolectric.testutils.fragments

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * An empty activity which acts as a container for fragments managed
 * by Hilt in tests.
 */
@AndroidEntryPoint
class TestFragmentActivity : AppCompatActivity()
package ua.cn.stu.espresso.apps.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.espresso.R

@AndroidEntryPoint
class CatsFragmentContainerActivity : AppCompatActivity(), FragmentRouter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragmentContainer, CatsListFragment())
            }
        }

        updateUi()
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallback, false)
        supportFragmentManager.addOnBackStackChangedListener {
            updateUi()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallback)
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

    override fun showDetails(catId: Long) {
        supportFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainer, CatDetailsFragment.newInstance(catId))
        }
    }

    private fun updateUi() {
        val showUp = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(showUp)
    }

    private val fragmentCallback = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            fragment: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, fragment, v, savedInstanceState)
            if (fragment is HasTitle) {
                supportActionBar?.title = fragment.title
            } else {
                supportActionBar?.title = getString(R.string.app_name)
            }
        }
    }

}
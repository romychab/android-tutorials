package ua.cn.stu.espresso.apps.navcomponent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.espresso.R

@AndroidEntryPoint
class NavComponentActivity : AppCompatActivity() {

    private val navController: NavController
        get() {
            val fragment = supportFragmentManager
                .findFragmentById(R.id.fragmentContainer) as NavHostFragment
            return fragment.navController
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_component)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

}
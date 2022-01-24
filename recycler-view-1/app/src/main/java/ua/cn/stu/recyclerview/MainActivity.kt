package ua.cn.stu.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import ua.cn.stu.recyclerview.databinding.ActivityMainBinding
import ua.cn.stu.recyclerview.model.User
import ua.cn.stu.recyclerview.model.UsersListener
import ua.cn.stu.recyclerview.model.UsersService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        adapter = UsersAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int, userPosition: Int) {
                usersService.moveUser(user, moveBy)
                // the workaround below retains the scroll position of the RecyclerView
                // when user moves the top visible item
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (userPosition == firstVisibleItemPosition || (userPosition == firstVisibleItemPosition + 1 && moveBy < 0)) {
                    val v = binding.recyclerView.getChildAt(0)
                    val offset = if (v == null) 0 else v.top - binding.recyclerView.paddingTop
                    layoutManager.scrollToPositionWithOffset(firstVisibleItemPosition, offset)
                }
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity, "User: ${user.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onUserFire(user: User) {
                usersService.fireUser(user)
            }
        })


        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        usersService.addListener(usersListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UsersListener = {
        adapter.users = it
    }
}
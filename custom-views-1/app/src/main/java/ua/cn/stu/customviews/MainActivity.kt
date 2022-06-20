package ua.cn.stu.customviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import ua.cn.stu.customviews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    /*private val token = Any()
    private val handler = Handler(Looper.getMainLooper())*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomButtons.setListener {
            if (it == BottomButtonAction.POSITIVE) {

                /*binding.bottomButtons.isProgressMode = true
                handler.postDelayed({
                    binding.bottomButtons.isProgressMode = false
                    binding.bottomButtons.setPositiveButtonText("Updated OK")
                    Toast.makeText(this, "Positive Button Pressed", Toast.LENGTH_SHORT).show()
                }, token, 2000)*/

                binding.bottomButtons.setPositiveButtonText("Updated OK")
                Toast.makeText(this, "Positive Button Pressed", Toast.LENGTH_SHORT).show()

            } else if (it == BottomButtonAction.NEGATIVE) {

                binding.bottomButtons.setNegativeButtonText("Updated Cancel")
                Toast.makeText(this, "Negative Button Pressed", Toast.LENGTH_SHORT).show()

            }
        }
    }

    /*override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(token)
    }*/

}
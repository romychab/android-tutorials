package ua.cn.stu.handler

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import ua.cn.stu.handler.databinding.ActivityHandlerBinding
import kotlin.random.Random

class HandlerLevel1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityHandlerBinding

    private val handler = Handler(Looper.getMainLooper())

    private val token = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHandlerBinding.inflate(layoutInflater).also { setContentView(it.root) }
        binding.root.forEach {
            if (it is Button) it.setOnClickListener(universalButtonListener)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun toggleTestButtonState() {
        binding.testButton.isEnabled = !binding.testButton.isEnabled
    }

    private fun nextRandomColor() {
        val randomColor = -Random.nextInt(255 * 255 * 255)
        binding.colorView.setBackgroundColor(randomColor)
    }

    private fun showToast() {
        Toast.makeText(this, R.string.hello, Toast.LENGTH_SHORT).show()
    }

    private val universalButtonListener = View.OnClickListener {
        Thread {
            when (it.id) {

                R.id.enableDisableButton ->
                    handler.post { toggleTestButtonState() }
                R.id.randomColorButton ->
                    handler.post { nextRandomColor() }

                R.id.enableDisableDelayedButton ->
                    handler.postDelayed({ toggleTestButtonState()}, DELAY)
                R.id.randomColorDelayedButton ->
                    handler.postDelayed({ nextRandomColor() }, DELAY)

                R.id.randomColorTokenDelayedButton ->
                    handler.postDelayed({ toggleTestButtonState()}, token, DELAY)
                R.id.showToastButton ->
                    handler.postDelayed({ showToast() }, token, DELAY)
                R.id.cancelButton -> handler.removeCallbacksAndMessages(token)

            }
        }.start()

    }

    companion object {
        @JvmStatic private val DELAY = 2000L // milliseconds
    }
}
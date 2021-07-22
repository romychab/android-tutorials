package ua.cn.stu.mvvmsavedstate

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.view.setMargins
import ua.cn.stu.mvvmsavedstate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.squares.observe(this) {
            renderSquares(it)
        }

        binding.generateColorsButton.setOnClickListener { viewModel.generateSquares() }
    }

    private fun renderSquares(squares: Squares) = with(binding) {
        colorsContainer.removeAllViews()
        val identifiers = squares.colors.indices.map { View.generateViewId() }
        for (i in squares.colors.indices) {
            val row = i / squares.size
            val column = i % squares.size

            val view = View(this@MainActivity)
            view.setBackgroundColor(squares.colors[i])
            view.id = identifiers[i]

            val params = LayoutParams(0, 0)
            params.setMargins(resources.getDimensionPixelSize(R.dimen.space))
            view.layoutParams = params

            // startToX constraint
            if (column == 0) params.startToStart = LayoutParams.PARENT_ID
            else params.startToEnd = identifiers[i - 1]

            // endToX constraint
            if (column == squares.size - 1) params.endToEnd = LayoutParams.PARENT_ID
            else params.endToStart = identifiers[i + 1]

            // topToX constraint
            if (row == 0) params.topToTop = LayoutParams.PARENT_ID
            else params.topToBottom = identifiers[i - squares.size]

            // bottomToX constraint
            if (row == squares.size - 1) params.bottomToBottom = LayoutParams.PARENT_ID
            else params.bottomToTop = identifiers[i + squares.size]

            colorsContainer.addView(view)
        }
    }


}
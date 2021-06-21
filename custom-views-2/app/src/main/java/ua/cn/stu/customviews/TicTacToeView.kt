package ua.cn.stu.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

typealias OnCellActionListener = (row: Int, column: Int, field: TicTacToeField) -> Unit

class TicTacToeView(
    context: Context,
    attributesSet: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attributesSet, defStyleAttr, defStyleRes) {

    // field data is stored here
    var field: TicTacToeField? = null
        set(value) {
            // removing listener from old field
            field?.listeners?.remove(listener)
            field = value
            // assigning listener to a new field
            value?.listeners?.add(listener)
            // new field may have another number of rows/columns, another cells, so need to update:
            updateViewSizes() // safe zone rect, cell size, cell padding
            requestLayout() // in case of using wrap_content, view size may also be changed
            invalidate() // redraw view
        }

    // assign your listener here in order to listen user actions with this view in your activity/fragment
    var actionListener: OnCellActionListener? = null

    // colors, may be initialized from attributes, styles, global style and default style
    private var player1Color by Delegates.notNull<Int>()
    private var player2Color by Delegates.notNull<Int>()
    private var gridColor by Delegates.notNull<Int>()

    // safe zone rect where we can drawing
    private val fieldRect = RectF(0f, 0f, 0f, 0f)
    // size of one cell
    private var cellSize: Float = 0f
    // padding in the cell
    private var cellPadding: Float = 0f

    // helper variable to avoid object allocation in onDraw
    private val cellRect = RectF()

    // current selected cell, useful when device doesn't have touchscreen
    private var currentRow: Int = -1
    private var currentColumn: Int = -1

    // pre-initialized paints for drawing
    private lateinit var player1Paint: Paint
    private lateinit var player2Paint: Paint
    private lateinit var currentCellPaint: Paint
    private lateinit var gridPaint: Paint

    // if there is no style, global style, color attributes -> then use values from DefaultTicTacToeFieldStyle
    constructor(context: Context, attributesSet: AttributeSet?, defStyleAttr: Int) : this(context, attributesSet, defStyleAttr, R.style.DefaultTicTacToeFieldStyle)

    // if global style is defined in app theme by using ticTacToeFieldStyle attribute ->
    //   all TicTacToe views in the project will use that style
    constructor(context: Context, attributesSet: AttributeSet?) : this(context, attributesSet, R.attr.ticTacToeFieldStyle)

    constructor(context: Context) : this(context, null)

    init {
        if (attributesSet != null) {
            initAttributes(attributesSet, defStyleAttr, defStyleRes)
        } else {
            initDefaultColors()
        }
        initPaints()

        if (isInEditMode) {
            // here we can initialize some data for component preview in Android Studio
            field = TicTacToeField(8, 6)
            field?.setCell(4, 2, Cell.PLAYER_1)
            field?.setCell(4, 3, Cell.PLAYER_2)
        }

        // make our view to work on devices without touchscreen
        isFocusable = true
        isClickable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            defaultFocusHighlightEnabled = false
        }
    }

    private fun initPaints() {
        // paint for drawing X
        player1Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        player1Paint.color = player1Color
        player1Paint.style = Paint.Style.STROKE
        player1Paint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)

        // paint for drawing O
        player2Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        player2Paint.color = player2Color
        player2Paint.style = Paint.Style.STROKE
        player2Paint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)

        // paint for drawing grid
        gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridPaint.color = gridColor
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)

        // paint for drawing current cell
        currentCellPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        currentCellPaint.color = Color.rgb(230, 230, 230)
        currentCellPaint.style = Paint.Style.FILL
    }

    private fun initAttributes(attributesSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attributesSet, R.styleable.TicTacToeView, defStyleAttr, defStyleRes)

        // parsing XML attributes
        player1Color = typedArray.getColor(R.styleable.TicTacToeView_player1Color, PLAYER1_DEFAULT_COLOR)
        player2Color = typedArray.getColor(R.styleable.TicTacToeView_player2Color, PLAYER2_DEFAULT_COLOR)
        gridColor = typedArray.getColor(R.styleable.TicTacToeView_gridColor, GRID_DEFAULT_COLOR)

        typedArray.recycle()
    }

    private fun initDefaultColors() {
        player1Color = PLAYER1_DEFAULT_COLOR
        player2Color = PLAYER2_DEFAULT_COLOR
        gridColor = GRID_DEFAULT_COLOR
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // start listening field data changes
        field?.listeners?.add(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // stop listening field data changes
        field?.listeners?.remove(listener)
    }

    override fun onSaveInstanceState(): Parcelable? {
        // example of saving view state; now we save only current row and current column position;
        // field data is saved/restored in the Activity
        val superState = super.onSaveInstanceState()!!
        val savedState = SavedState(superState)
        savedState.currentRow = currentRow
        savedState.currentColumn = currentColumn
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        // example of restoring view state
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        currentRow = savedState.currentRow
        currentColumn = savedState.currentColumn
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        // here we can submit our suggestion how large our view should be

        // min size of our view
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        // calculating desired size of view
        val desiredCellSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DESIRED_CELL_SIZE,
                resources.displayMetrics).toInt()
        val rows = field?.rows ?: 0
        val columns = field?.columns ?: 0

        val desiredWith = max(minWidth, columns * desiredCellSizeInPixels + paddingLeft + paddingRight)
        val desiredHeight = max(minHeight, rows * desiredCellSizeInPixels + paddingTop + paddingBottom)

        // submit view size
        setMeasuredDimension(
            resolveSize(desiredWith, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // here we have the real view size after all calculations;
        // whenever view size is changed we need to recalculate safe zone and cell size/padding
        updateViewSizes()
    }

    private fun updateViewSizes() {
        val field = this.field ?: return

        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        val cellWidth = safeWidth / field.columns.toFloat()
        val cellHeight = safeHeight / field.rows.toFloat()

        cellSize = min(cellWidth, cellHeight)
        cellPadding = cellSize * 0.2f

        val fieldWidth = cellSize * field.columns
        val fieldHeight = cellSize * field.rows

        fieldRect.left = paddingLeft + (safeWidth - fieldWidth) / 2
        fieldRect.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRect.right = fieldRect.left + fieldWidth
        fieldRect.bottom = fieldRect.top + fieldHeight
    }

    // DRAWING SECTION

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (field == null) return
        if (cellSize == 0f) return
        if (fieldRect.width() <= 0) return
        if (fieldRect.height() <= 0) return

        drawGrid(canvas)
        drawCurrentCell(canvas)
        drawCells(canvas)
    }

    private fun drawCurrentCell(canvas: Canvas) {
        val field = this.field ?: return
        if (currentRow < 0 && currentColumn < 0 && currentRow >= field.rows
                && currentColumn >= field.columns) return

        val cell = getCellRect(currentRow, currentColumn)
        canvas.drawRect(
                cell.left - cellPadding,
                cell.top - cellPadding,
                cell.right + cellPadding,
                cell.bottom + cellPadding,
                currentCellPaint
        )
    }

    private fun drawGrid(canvas: Canvas) {
        val field = this.field ?: return

        val xStart = fieldRect.left
        val xEnd = fieldRect.right
        for (i in 0..field.rows) {
            val y = fieldRect.top + cellSize * i
            canvas.drawLine(xStart, y, xEnd, y, gridPaint)
        }

        val yStart = fieldRect.top
        val yEnd = fieldRect.bottom
        for (i in 0..field.columns) {
            val x = fieldRect.left + cellSize * i
            canvas.drawLine(x, yStart, x, yEnd, gridPaint)
        }
    }

    private fun drawCells(canvas: Canvas) {
        val field = this.field ?: return
        for (row in 0 until field.rows) {
            for (column in 0 until field.columns) {
                val cell = field.getCell(row, column)
                if (cell == Cell.PLAYER_1) {
                    drawPlayer1(canvas, row, column)
                } else if (cell == Cell.PLAYER_2) {
                    drawPlayer2(canvas, row, column)
                }
            }
        }
    }

    private fun drawPlayer1(canvas: Canvas, row: Int, column: Int) {
        val cellRect = getCellRect(row, column)
        canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.bottom, player1Paint)
        canvas.drawLine(cellRect.right, cellRect.top, cellRect.left, cellRect.bottom, player1Paint)
    }

    private fun drawPlayer2(canvas: Canvas, row: Int, column: Int) {
        val cellRect = getCellRect(row, column)
        canvas.drawCircle(cellRect.centerX(), cellRect.centerY(), cellRect.width() / 2, player2Paint)
    }

    // EVENTS HANDLING SECTION

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when(keyCode) {
            KeyEvent.KEYCODE_DPAD_DOWN -> moveCurrentCell(1, 0)
            KeyEvent.KEYCODE_DPAD_LEFT -> moveCurrentCell(0, -1)
            KeyEvent.KEYCODE_DPAD_RIGHT -> moveCurrentCell(0, 1)
            KeyEvent.KEYCODE_DPAD_UP -> moveCurrentCell(-1, 0)
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                updateCurrentCell(event)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                updateCurrentCell(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                return performClick()
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        val field = this.field ?: return false
        val row = currentRow
        val column = currentColumn
        if (row >= 0 && column >= 0 && row < field.rows && column < field.columns) {
            actionListener?.invoke(row, column, field)
            return true
        }
        return false
    }

    private fun moveCurrentCell(rowDiff: Int, columnDiff: Int): Boolean {
        val field = this.field ?: return false
        if (currentRow < 0 || currentColumn < 0 || currentRow >= field.rows
                || currentColumn >= field.columns) {
            currentRow = 0
            currentColumn = 0
            invalidate()
            return true
        } else {
            if (currentColumn + columnDiff < 0) return false
            if (currentColumn + columnDiff >= field.columns) return false
            if (currentRow + rowDiff < 0) return false
            if (currentRow + rowDiff >= field.rows) return false

            currentColumn += columnDiff
            currentRow += rowDiff
            invalidate()
            return true
        }
    }

    private fun updateCurrentCell(event: MotionEvent) {
        val field = this.field ?: return
        val row = getRow(event)
        val column = getColumn(event)
        if (row >= 0 && column >= 0 && row < field.rows && column < field.columns) {
            if (currentRow != row || currentColumn != column) {
                currentRow = row
                currentColumn = column
                invalidate()
            }
        } else {
            // clearing current cell if user moves out from the view
            currentRow = -1
            currentColumn = -1
            invalidate()
        }
    }

    // HELPER METHODS

    private fun getRow(event: MotionEvent): Int {
        // floor is better then simple rounding to int in our case
        // because it rounds to an integer towards negative infinity
        // examples:
        // 1) -0.3.toInt() = 0
        // 2) floor(-0.3) = -1
        return floor((event.y - fieldRect.top) / cellSize).toInt()
    }

    private fun getColumn(event: MotionEvent): Int {
        return floor((event.x - fieldRect.left) / cellSize).toInt()
    }

    private fun getCellRect(row: Int, column: Int): RectF {
        cellRect.left = fieldRect.left + column * cellSize + cellPadding
        cellRect.top = fieldRect.top + row * cellSize + cellPadding
        cellRect.right = cellRect.left + cellSize - cellPadding * 2
        cellRect.bottom = cellRect.top + cellSize - cellPadding * 2
        return cellRect
    }

    // when some data changes in the field -> need to redraw the view
    private val listener: OnFieldChangedListener = {
        invalidate()
    }

    // ---

    class SavedState : BaseSavedState {

        var currentRow by Delegates.notNull<Int>()
        var currentColumn by Delegates.notNull<Int>()

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            currentRow = parcel.readInt()
            currentColumn = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(currentRow)
            out.writeInt(currentColumn)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = Array(size) { null }
            }
        }

    }

    companion object {
        const val PLAYER1_DEFAULT_COLOR = Color.GREEN
        const val PLAYER2_DEFAULT_COLOR = Color.RED
        const val GRID_DEFAULT_COLOR = Color.GRAY

        const val DESIRED_CELL_SIZE = 50f
    }
}
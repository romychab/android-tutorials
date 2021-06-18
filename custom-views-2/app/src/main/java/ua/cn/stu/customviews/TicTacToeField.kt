package ua.cn.stu.customviews

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class Cell {
    PLAYER_1, // X
    PLAYER_2, // O
    EMPTY     // empty cell
}

typealias OnFieldChangedListener = (field: TicTacToeField) -> Unit

/**
 * Data class for [TicTacToeView]
 */
class TicTacToeField private constructor(
    val rows: Int,
    val columns: Int,
    private val cells: Array<Array<Cell>>
) {

    val listeners = mutableSetOf<OnFieldChangedListener>()

    constructor(rows: Int, columns: Int) : this(
        rows,
        columns,
        Array(rows) { Array(columns) { Cell.EMPTY } }
    )

    fun getCell(row: Int, column: Int): Cell {
        if (row < 0 || column < 0 || row >= rows || column >= columns) return Cell.EMPTY
        return cells[row][column]
    }

    fun setCell(row: Int, column: Int, cell: Cell) {
        if (row < 0 || column < 0 || row >= rows || column >= columns) return
        if (cells[row][column] != cell) {
            cells[row][column] = cell
            listeners.forEach { it?.invoke(this) }
        }
    }

    fun saveState(): Memento {
        // ideally cells should be copied (e.g. Arrays.copyOf)
        return Memento(rows, columns, cells)
    }

    @Suppress("ArrayInDataClass")
    @Parcelize
    data class Memento(
        private val rows: Int,
        private val columns: Int,
        private val cells: Array<Array<Cell>>
    ) : Parcelable {
        fun restoreField(): TicTacToeField {
            // ideally cells should be copied (e.g. Arrays.copyOf)
            return TicTacToeField(rows, columns, cells)
        }
    }

}
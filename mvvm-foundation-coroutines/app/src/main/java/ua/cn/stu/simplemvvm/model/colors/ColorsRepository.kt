package ua.cn.stu.simplemvvm.model.colors

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.foundation.model.Repository

/**
 * Repository interface example.
 *
 * Provides access to the available colors and current selected color.
 */
interface ColorsRepository : Repository {

    /**
     * Get the list of all available colors that may be chosen by the user.
     */
    suspend fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color content by its ID
     */
    suspend fun getById(id: Long): NamedColor

    /**
     * Get the current selected color.
     */
    suspend fun getCurrentColor(): NamedColor

    /**
     * Set the specified color as current.
     * @return [Flow] which periodically emits the current progress of the operation in percents.
     */
    fun setCurrentColor(color: NamedColor): Flow<Int>

    /**
     * Listen for further changes of the current color.
     * @return [Flow] which emits a new item whenever [setCurrentColor] call
     * changes the current color.
     */
    fun listenCurrentColor(): Flow<NamedColor>

}
package ua.cn.stu.simplemvvm.model.colors

import android.graphics.Color
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ua.cn.stu.foundation.model.coroutines.IoDispatcher

/**
 * Simple in-memory implementation of [ColorsRepository]
 */
class InMemoryColorsRepository(
    private val ioDispatcher: IoDispatcher
) : ColorsRepository {

    private var currentColor: NamedColor = AVAILABLE_COLORS[0]

    private val currentColorFlow = MutableSharedFlow<NamedColor>(
        replay = 0, // do not send events to new subscribers which have been emitted before subscription
        extraBufferCapacity = 1, // min. buffer capacity for using DROP_OLDEST overflow policy
        onBufferOverflow = BufferOverflow.DROP_OLDEST // newest item will replace oldest item in case of buffer overflow
    )

    override fun listenCurrentColor(): Flow<NamedColor> = currentColorFlow

    override suspend fun getAvailableColors(): List<NamedColor> = withContext(ioDispatcher.value) {
        delay(1000)
        return@withContext AVAILABLE_COLORS
    }

    override suspend fun getById(id: Long): NamedColor = withContext(ioDispatcher.value) {
        delay(100)
        return@withContext AVAILABLE_COLORS.first { it.id == id }
    }

    override suspend fun getCurrentColor(): NamedColor = withContext(ioDispatcher.value) {
        delay(1000)
        return@withContext currentColor
    }

    override fun setCurrentColor(color: NamedColor): Flow<Int> = flow {
        if (currentColor != color) {
            var progress = 0
            while (progress < 100) {
                progress += 2
                delay(30)
                emit(progress)
            }
            currentColor = color
            currentColorFlow.emit(color)
        } else {
            emit(100)
        }
    }.flowOn(ioDispatcher.value)

    companion object {
        private val AVAILABLE_COLORS = listOf(
            NamedColor(1, "Red", Color.RED),
            NamedColor(2, "Green", Color.GREEN),
            NamedColor(3, "Blue", Color.BLUE),
            NamedColor(4, "Yellow", Color.YELLOW),
            NamedColor(5, "Magenta", Color.MAGENTA),
            NamedColor(6, "Cyan", Color.CYAN),
            NamedColor(7, "Gray", Color.GRAY),
            NamedColor(8, "Navy", Color.rgb(0, 0, 128)),
            NamedColor(9, "Pink", Color.rgb(255, 20, 147)),
            NamedColor(10, "Sienna", Color.rgb(160, 82, 45)),
            NamedColor(11, "Khaki", Color.rgb(240, 230, 140)),
            NamedColor(12, "Forest Green", Color.rgb(34, 139, 34)),
            NamedColor(13, "Sky", Color.rgb(135, 206, 250)),
            NamedColor(14, "Olive", Color.rgb(107, 142, 35)),
            NamedColor(15, "Violet", Color.rgb(148, 0, 211)),
        )
    }
}
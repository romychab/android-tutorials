package ua.cn.stu.simplemvvm.model.colors

import android.graphics.Color
import ua.cn.stu.foundation.model.tasks.Task
import ua.cn.stu.foundation.model.tasks.ThreadUtils
import ua.cn.stu.foundation.model.tasks.factories.TasksFactory

/**
 * Simple in-memory implementation of [ColorsRepository]
 */
class InMemoryColorsRepository(
    private val tasksFactory: TasksFactory,
    private val threadUtils: ThreadUtils
) : ColorsRepository {

    private var currentColor: NamedColor = AVAILABLE_COLORS[0]

    private val listeners = mutableSetOf<ColorListener>()

    override fun addListener(listener: ColorListener) {
        listeners += listener
    }

    override fun removeListener(listener: ColorListener) {
        listeners -= listener
    }

    override fun getAvailableColors(): Task<List<NamedColor>> = tasksFactory.async {
        threadUtils.sleep(1000)
        return@async AVAILABLE_COLORS
    }

    override fun getById(id: Long): Task<NamedColor> = tasksFactory.async {
        threadUtils.sleep(1000)
        return@async AVAILABLE_COLORS.first { it.id == id }
    }

    override fun getCurrentColor(): Task<NamedColor> = tasksFactory.async {
        threadUtils.sleep(1000)
        return@async currentColor
    }

    override fun setCurrentColor(color: NamedColor): Task<Unit> = tasksFactory.async {
        threadUtils.sleep(1000)
        if (currentColor != color) {
            currentColor = color
            listeners.forEach { it(color) }
        }
    }

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
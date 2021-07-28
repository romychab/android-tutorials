package ua.cn.stu.simplemvvm.model.colors

import android.graphics.Color

/**
 * Simple in-memory implementation of [ColorsRepository]
 */
class InMemoryColorsRepository : ColorsRepository {

    override var currentColor: NamedColor = AVAILABLE_COLORS[0]
        set(value) {
            if (field != value) {
                field = value
                listeners.forEach { it(value) }
            }
        }

    private val listeners = mutableSetOf<ColorListener>()

    override fun getAvailableColors(): List<NamedColor> = AVAILABLE_COLORS

    override fun addListener(listener: ColorListener) {
        listeners += listener
        listener(currentColor)
    }

    override fun removeListener(listener: ColorListener) {
        listeners -= listener
    }

    override fun getById(id: Long): NamedColor {
        return AVAILABLE_COLORS.first { it.id == id }
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
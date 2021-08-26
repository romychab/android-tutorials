package ua.cn.stu.simplemvvm.views.changecolor

import ua.cn.stu.simplemvvm.model.colors.NamedColor

/**
 * Represents list item for the color; it may be selected or not
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)
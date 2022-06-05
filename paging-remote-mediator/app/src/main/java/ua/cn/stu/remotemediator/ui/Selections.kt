package ua.cn.stu.remotemediator.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ua.cn.stu.remotemediator.ui.base.OnChange

interface SelectionState {

    /**
     * Whether the item is checked or not.
     */
    fun isChecked(id: Long): Boolean
}

/**
 * In-memory handling of check state.
 */
class Selections : SelectionState {

    private val checkedIds = mutableSetOf<Long>()
    private val checkedIdsFlow =
        MutableStateFlow(OnChange(checkedIds))

    /**
     * Select the item if it is not selected and vise versa.
     */
    fun toggle(id: Long) {
        if (checkedIds.contains(id)) {
            checkedIds.remove(id)
        } else {
            checkedIds.add(id)
        }
        checkedIdsFlow.value = OnChange(checkedIds)
    }

    /**
     * Whether the item is selected or not.
     */
    override fun isChecked(id: Long) = checkedIds.contains(id)

    /**
     * Listen for the selection state changes.
     */
    fun flow(): Flow<SelectionState> = checkedIdsFlow.map { this }

}
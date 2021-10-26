package ua.cn.stu.navcomponent.tabs.model.boxes

import android.graphics.Color
import kotlinx.coroutines.flow.*
import ua.cn.stu.navcomponent.tabs.R
import ua.cn.stu.navcomponent.tabs.model.accounts.AccountsRepository
import ua.cn.stu.navcomponent.tabs.model.boxes.entities.Box

class InMemoryBoxesRepository(
    private val accountsRepository: AccountsRepository
) : BoxesRepository {

    private val boxes = listOf(
        Box(1, R.string.green, Color.rgb(0, 128, 0)),
        Box(2, R.string.red, Color.rgb(128, 0, 0)),
        Box(3, R.string.blue, Color.rgb(0, 0, 128)),
        Box(4, R.string.yellow, Color.rgb(128, 128, 0)),
        Box(5, R.string.black, Color.rgb(0, 0, 0)),
        Box(6, R.string.violet, Color.rgb(128, 0, 255))
    )

    private val allActiveBoxes: MutableMap<String, MutableSet<Int>> = mutableMapOf()
    private val reconstructFlow = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    private val activeBoxesFlow: Flow<Set<Int>> = combine(reconstructFlow, accountsRepository.getAccount()) { _, account ->
        if (account == null) return@combine emptySet<Int>()
        val activeIds = allActiveBoxes[account.email] ?: let {
            val newActiveIdsSet = mutableSetOf<Int>()
            newActiveIdsSet.addAll(boxes.map { it.id })
            allActiveBoxes[account.email] = newActiveIdsSet
            newActiveIdsSet
        }
        return@combine HashSet(activeIds)
    }

    override fun getBoxes(onlyActive: Boolean): Flow<List<Box>> = activeBoxesFlow.map { activeIdentifiers ->
        boxes.filter { if (onlyActive) activeIdentifiers.contains(it.id) else true }
    }

    override suspend fun activateBox(box: Box) {
        val account = accountsRepository.getAccount().firstOrNull() ?: return
        val activeBoxes = allActiveBoxes[account.email] ?: return
        activeBoxes.add(box.id)
        reconstructFlow.tryEmit(Unit)
    }

    override suspend fun deactivateBox(box: Box) {
        val account = accountsRepository.getAccount().firstOrNull() ?: return
        val activeBoxes = allActiveBoxes[account.email] ?: return
        activeBoxes.remove(box.id)
        reconstructFlow.tryEmit(Unit)
    }

}
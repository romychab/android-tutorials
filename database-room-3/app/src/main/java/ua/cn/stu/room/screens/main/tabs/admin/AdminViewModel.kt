package ua.cn.stu.room.screens.main.tabs.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.cn.stu.room.R
import ua.cn.stu.room.model.accounts.AccountsRepository
import ua.cn.stu.room.model.accounts.entities.Account
import ua.cn.stu.room.model.accounts.entities.AccountFullData
import ua.cn.stu.room.model.boxes.entities.Box
import ua.cn.stu.room.model.boxes.entities.BoxAndSettings
import ua.cn.stu.room.screens.main.tabs.admin.ExpansionStatus.*
import ua.cn.stu.room.utils.resources.Resources
import ua.cn.stu.room.utils.share

class AdminViewModel(
    private val accountsRepository: AccountsRepository,
    private val resources: Resources
) : ViewModel(), AdminItemsAdapter.Listener {

    private val _items = MutableLiveData<List<AdminTreeItem>>()
    val items = _items.share()

    // here we store IDs of items which are expanded
    private val expandedIdentifiers = mutableSetOf(getRootId())
    // state flow is used for notifying about changes when some item expands/collapses
    private val expandedItemsStateFlow = MutableStateFlow(ExpansionsState(expandedIdentifiers))

    init {
        viewModelScope.launch {
            // combining 2 flow:
            // - the first flow notifies about data changes from the repository
            // - the second flow notifies about expand/collapse status changes
            combine(accountsRepository.getAllData(), expandedItemsStateFlow) { allData, expansionsState ->
                    // in order to simplify the logic of creating list for RecyclerView
                    // let's divide this into 2 steps

                    // 1st step: create a tree structure with nodes of the same type
                    val rootNode = toNode(allData, expansionsState.identifiers)

                    // 2nd step: convert the tree into a simple flat list
                    flatNodes(rootNode)
                }
                .collect {
                    // assign list to be rendered in the RecyclerView
                    _items.value = it
                }
        }
    }

    /**
     * If item is collapsed -> expand it.
     * If item is expanded -> collapse it.
     */
    override fun onItemToggled(item: AdminTreeItem) {
        if (item.expansionStatus == NOT_EXPANDABLE) return

        if (item.expansionStatus == EXPANDED) {
            expandedIdentifiers.remove(item.id)
        } else {
            expandedIdentifiers.add(item.id)
        }
        expandedItemsStateFlow.value = ExpansionsState(expandedIdentifiers)
    }

    /**
     * Building a real data tree nodes.
     */
    private fun toNode(accountsDataList: List<AccountFullData>, expandedIdentifiers: Set<Long>): Node {
        val rootExpansion = getExpansionStatus(getRootId(), accountsDataList.isNotEmpty(), expandedIdentifiers)
        return Node(
            id = getRootId(),
            attributes = mapOf(resources.getString(R.string.all_accounts) to ""),
            expansionStatus = rootExpansion,
            nodes = if (rootExpansion != EXPANDED) emptyList() else accountsDataList.map { accountsData ->
                val accountExpansionStatus = getExpansionStatus(getAccountId(accountsData), accountsData.boxesAndSettings.isNotEmpty(), expandedIdentifiers)
                Node(
                    id = getAccountId(accountsData),
                    attributes = accountToMap(accountsData.account),
                    expansionStatus = accountExpansionStatus,
                    nodes = if (accountExpansionStatus != EXPANDED) emptyList() else accountsData.boxesAndSettings.map { boxAndSettings ->
                        val boxExpansionStatus = getExpansionStatus(getBoxId(boxAndSettings), true, expandedIdentifiers)
                        Node(
                            id = getBoxId(boxAndSettings),
                            attributes = boxToMap(boxAndSettings.box),
                            expansionStatus = boxExpansionStatus,
                            nodes = if (boxExpansionStatus != EXPANDED) emptyList() else listOf(
                                Node(
                                    id = getSettingsId(boxAndSettings, accountsData),
                                    attributes = settingsToMap(boxAndSettings.isActive),
                                    expansionStatus = NOT_EXPANDABLE,
                                    nodes = emptyList()
                                )
                            )
                        )
                    }
                )
            }
        )
    }

    /**
     * RecyclerView with DiffUtil are not intended to work with tree-based structures
     * so let's convert the tree starting from the root node into a simple flat list.
     */
    private fun flatNodes(root: Node): List<AdminTreeItem> {
        val items = mutableListOf<AdminTreeItem>()
        val level = 0
        doFlatNodes(root, level, items)
        return items
    }

    /**
     * Helper recursive method for the previous one.
     */
    private fun doFlatNodes(node: Node, level: Int, items: MutableList<AdminTreeItem>) {
        val item = AdminTreeItem(
            id = node.id,
            level = level,
            expansionStatus = node.expansionStatus,
            attributes = node.attributes
        )
        items.add(item)
        for (child in node.nodes) {
            doFlatNodes(child, level + 1, items)
        }
    }

    // --- identifiers should be unique for each item in the tree to be correctly rendered by DiffUtil

    private fun getRootId(): Long = 1L

    private fun getAccountId(accountFullData: AccountFullData): Long = accountFullData.account.id or ACCOUNT_MASK

    private fun getBoxId(boxAndSettings: BoxAndSettings): Long = boxAndSettings.box.id or BOX_MASK

    private fun getSettingsId(boxAndSettings: BoxAndSettings, accountFullData: AccountFullData): Long =
        boxAndSettings.box.id or SETTING_MASK or (accountFullData.account.id shl 32)

    // ---

    private fun getExpansionStatus(id: Long, hasChildren: Boolean, expandedIds: Set<Long>): ExpansionStatus {
        if (!hasChildren) return NOT_EXPANDABLE
        return if (expandedIds.contains(id)) {
            EXPANDED
        } else {
            COLLAPSED
        }
    }

    private fun accountToMap(account: Account): Map<String, String> {
        return mapOf(
            resources.getString(R.string.account_id) to account.id.toString(),
            resources.getString(R.string.account_email) to account.email,
            resources.getString(R.string.account_username) to account.username
        )
    }

    private fun boxToMap(box: Box): Map<String, String> {
        return mapOf(
            resources.getString(R.string.box_id) to box.id.toString(),
            resources.getString(R.string.box_name) to box.colorName,
            resources.getString(R.string.box_value) to String.format("#%06X", (0xFFFFFF and box.colorValue))
        )
    }

    private fun settingsToMap(isActive: Boolean): Map<String, String> {
        val isActiveString = resources.getString(if (isActive) R.string.yes else R.string.no)
        return mapOf(
            resources.getString(R.string.setting_is_active) to isActiveString
        )
    }

    private class Node(
        val id: Long,
        val attributes: Map<String, String>,
        val expansionStatus: ExpansionStatus,
        val nodes: List<Node>
    )

    private class ExpansionsState(
        val identifiers: Set<Long>
    )

    private companion object {
        const val ACCOUNT_MASK = 0x2L shl 60
        const val BOX_MASK = 0x3L shl 60
        const val SETTING_MASK = 0x4L shl 60
    }

}
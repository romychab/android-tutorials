package ua.cn.stu.multimodule.glue.catalog.mappers

import ua.cn.stu.multimodule.catalog.domain.entities.SortOrder
import ua.cn.stu.multimodule.data.products.entities.SortOrderDataValue
import javax.inject.Inject

class SortOrderMapper @Inject constructor() {

    fun toSortOrderDataValue(sortOrder: SortOrder): SortOrderDataValue {
        return when (sortOrder) {
            SortOrder.DESC -> SortOrderDataValue.DESC
            SortOrder.ASC -> SortOrderDataValue.ASC
        }
    }

}
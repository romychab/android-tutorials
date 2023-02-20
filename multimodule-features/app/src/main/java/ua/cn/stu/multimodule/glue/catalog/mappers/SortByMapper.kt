package ua.cn.stu.multimodule.glue.catalog.mappers

import ua.cn.stu.multimodule.catalog.domain.entities.SortBy
import ua.cn.stu.multimodule.data.products.entities.SortByDataValue
import javax.inject.Inject

class SortByMapper @Inject constructor() {

    fun toSortByDataValue(sortBy: SortBy): SortByDataValue {
        return when (sortBy) {
            SortBy.PRICE -> SortByDataValue.PRICE
            SortBy.NAME -> SortByDataValue.NAME
        }
    }

}
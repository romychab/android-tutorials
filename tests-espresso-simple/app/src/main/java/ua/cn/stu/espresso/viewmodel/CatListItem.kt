package ua.cn.stu.espresso.viewmodel

sealed class CatListItem {

    data class Header(
        val headerId: Int,
        val fromIndex: Int,
        val toIndex: Int
    ) : CatListItem()

    data class Cat(
        val originCat: ua.cn.stu.espresso.model.Cat
    ) : CatListItem() {
        val id: Long get() = originCat.id
        val name: String get() = originCat.name
        val photoUrl: String get() = originCat.photoUrl
        val description: String get() = originCat.description
        val isFavorite: Boolean get() = originCat.isFavorite
    }

}
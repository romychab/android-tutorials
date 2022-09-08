package com.elveum.elementadapter.app.model

import com.github.javafaker.Faker
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatsRepository @Inject constructor() {

    private val photos = listOf(
        "https://images.unsplash.com/photo-1615454299901-de13b71ecaae?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODAyNw&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/photo-1604916287784-c324202b3205?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODAzMQ&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/photo-1596854372407-baba7fef6e51?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODAzOA&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/photo-1608032364895-0da67af36cd2?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODA0Mw&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/photo-1571988840298-3b5301d5109b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODA0Ng&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/photo-1494256997604-768d1f608cac?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODEyMQ&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/flagged/photo-1557427161-4701a0fa2f42?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODEyNQ&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/photo-1640384974326-3e72680e0fb3?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODI0MA&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080",
        "https://images.unsplash.com/photo-1623876159473-5e79be88f7ac?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxfDB8MXxyYW5kb218MHx8Y2F0fHx8fHx8MTY2MjQ3ODIzMw&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=1080"
    )

    private val random = Random(42)
    private val faker = Faker.instance(random)

    private val catsFlow = MutableStateFlow(
        List(100) { index -> randomCat(id = index + 1L) }
    )

    fun getCats(): Flow<List<Cat>> {
        return catsFlow
    }

    fun delete(cat: Cat) {
        catsFlow.update { oldList ->
            oldList.filter { it.id != cat.id }
        }
    }

    fun toggleIsFavorite(cat: Cat) {
        catsFlow.update { oldList ->
            oldList.map {
                if (it.id == cat.id) {
                    cat.copy(isFavorite = !cat.isFavorite)
                } else {
                    it
                }
            }
        }
    }

    private fun randomCat(id: Long): Cat = Cat(
        id = id,
        name = faker.cat().name(),
        photoUrl = photos[(id.rem(photos.size)).toInt()],
        isFavorite = false,
        description = faker.lorem().sentences(2).joinToString(" ")
    )

}
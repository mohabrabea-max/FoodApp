package com.example.applicationhome.fakes

import com.example.applicationhome.data.data.model.CategoriesInWithTitle
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.RestaurantsEntity

object FakeRestaurants {
    fun restaurantsEntityFakes() = listOf(
        RestaurantsEntity(
            id = 1,
            name = "aaa",
            typ = listOf(
                CategoriesInWithTitle(
                    title = "pizza",
                    category = "pizza",
                    index = 0
                ),
                CategoriesInWithTitle(
                    title = "burger",
                    category = "burger",
                    index = 1
                ),
                CategoriesInWithTitle(
                    title = "chicken",
                    category = "chicken",
                    index = 2
                )
            )
        ),
        RestaurantsEntity(
            id = 2,
            name = "bbb",
            typ = listOf(
                CategoriesInWithTitle(
                    title = "burger",
                    category = "burger",
                    index = 0
                ),
                CategoriesInWithTitle(
                    title = "pizza",
                    category = "pizza",
                    index = 1
                ),
                CategoriesInWithTitle(
                    title = "chicken",
                    category = "chicken",
                    index = 2
                )
            )
        ),
        RestaurantsEntity(
            id = 3,
            name = "ccc",
            typ = listOf(
                CategoriesInWithTitle(
                    title = "chicken",
                    category = "chicken",
                    index = 0
                ),
                CategoriesInWithTitle(
                    title = "burger",
                    category = "burger",
                    index = 1
                ),
                CategoriesInWithTitle(
                    title = "pizza",
                    category = "pizza",
                    index = 2
                )
            )
        ),
    )

    fun favoriteRestaurantEntityFakes() = listOf(
        FavoriteRestaurantEntity(
            resId = 1,
            userId = "abcd"
        ),
        FavoriteRestaurantEntity(
            resId = 2,
            userId = "abcd"
        ),
        FavoriteRestaurantEntity(
            resId = 3,
            userId = "abcd"
        )
    )
}
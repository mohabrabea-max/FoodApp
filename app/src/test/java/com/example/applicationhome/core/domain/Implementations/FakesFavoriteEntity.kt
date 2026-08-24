package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnacksEntity

object FakesFavoriteEntity {
    fun fakesMealsFavoriteEntity() =
        listOf(
            MealWithFavoriteStatus(
                meal = MealsEntity(id = 1),
                favoriteInfo = FavoriteMealEntity(
                    mealId = 1,
                    userId = "aaaaa"
                )
            ),
            MealWithFavoriteStatus(
                meal = MealsEntity(id = 2),
                favoriteInfo = FavoriteMealEntity(
                    mealId = 2,
                    userId = "aaaaa"
                )
            ),
            MealWithFavoriteStatus(
                meal = MealsEntity(id = 3),
                favoriteInfo = FavoriteMealEntity(
                    mealId = 3,
                    userId = "aaaaa"
                )
            )
        )

    fun fakesSnacksFavoriteEntity() =
        listOf(
            SnackWithFavoriteStatus(
                snack = SnacksEntity(id = 4),
                favoriteInfo = FavoriteSnackEntity(
                    snackId = 4,
                    userId = "aaaaa"
                )
            ),
            SnackWithFavoriteStatus(
                snack = SnacksEntity(id = 5),
                favoriteInfo = FavoriteSnackEntity(
                    snackId = 5,
                    userId = "aaaaa"
                )
            )
        )

    fun fakesRestaurantsFavoriteEntity() =
        listOf(
            RestaurantWithFavoriteStatus(
                restaurant = RestaurantsEntity(id = 5),
                favoriteInfo = FavoriteRestaurantEntity(
                    resId = 5,
                    userId = "aaaaa"
                )
            ),
            RestaurantWithFavoriteStatus(
                restaurant = RestaurantsEntity(id = 6),
                favoriteInfo = FavoriteRestaurantEntity(
                    resId = 6,
                    userId = "aaaaa"
                )
            ),
            RestaurantWithFavoriteStatus(
                restaurant = RestaurantsEntity(id = 7),
                favoriteInfo = FavoriteRestaurantEntity(
                    resId = 7,
                    userId = "aaaaa"
                )
            ),
            RestaurantWithFavoriteStatus(
                restaurant = RestaurantsEntity(id = 8),
                favoriteInfo = FavoriteRestaurantEntity(
                    resId = 8,
                    userId = "aaaaa"
                )
            ),
        )
}
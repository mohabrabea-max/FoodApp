package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.data.local.entity.CartClass
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.SnackDomain

object FakeCartData {
    fun fakeCartRestaurant() =
        CartClass(
            userId = "aaaaa",
            restaurantId = 1
        )


    fun fakeCartItemsDomainClass() =
        CartItemsDomainClass(
            mealKey = "1_",
            mealId = 1,
            quantity = 5,
            priceOfOne = 10.0,
            restaurantId = 1
        )

    fun mealDomain() =
        MealDomain(
            id = 1,
            restaurantId = 1
        )

    fun snackDomain() =
        SnackDomain(
            id = 1,
            restaurantId = 1
        )
}
package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.data.local.entity.CartClass
import com.example.applicationhome.data.local.entity.CartItemsClass

object FakeCartData {
    fun fakeCartRestaurant() =
        CartClass(
            userId = "aaaaa",
            restaurantId = 1
        )


    fun fakeItems() =
        CartItemsClass(
            mealKey = "1_",
            mealId = 1,
            quantity = 5,
            priceOfOne = 10.0,
            restaurantId = 1
        )
}
package com.example.applicationhome.core.ui.mapper

import com.example.applicationhome.core.data.mapper.discountsDomainClassToDiscountsUi
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.SnackDomain
import com.example.applicationhome.core.ui.model.CartItemsUiClass
import com.example.applicationhome.core.ui.model.FoodItem

fun MealDomain.mealDomainToUiModel(): FoodItem.MealItem =
    FoodItem.MealItem(
        id = this.id,
        name = this.name,
        details = this.details,
        image = this.image,
        sizes = this.sizeOptions.associate { it.size to it.price },
        restaurantId = this.restaurantId,
        review = this.review,
        discount = this.discount?.discountsDomainClassToDiscountsUi(),
        isFavorite = this.isFavorite,
        category = this.category,
        sizeOptions = this.sizeOptions
    )

fun SnackDomain.snackDomainToUiModel(): FoodItem.SnackItem =
    FoodItem.SnackItem(
        id = this.id,
        name = this.name,
        details = this.details,
        image = this.image,
        sizes = this.priceANDsize,
        restaurantId = this.restaurantId,
        review = this.review,
        discount = this.discount?.discountsDomainClassToDiscountsUi(),
        isFavorite = this.isFavorite
    )

fun CartItemsDomainClass.cartItemsDomainClassToCartItemsUiClass(): CartItemsUiClass  =
    CartItemsUiClass(
        userId = this.userId,
        mealKey = this.mealKey,
        mealId = this.mealId,
        name = this.name,
        type = this.type,
        size = this.size,
        quantity = this.quantity,
        priceOfOne = this.priceOfOne,
        totalPrice = this.totalPrice,
        image = this.image,
        restaurantId = this.restaurantId,
        discount = this.discount?.discountsDomainClassToDiscountsUi(),
        finalPrice = this.finalPriceOfOne
    )
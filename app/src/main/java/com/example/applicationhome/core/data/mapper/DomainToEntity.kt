package com.example.applicationhome.core.data.mapper

import com.example.applicationhome.core.data.local.entity.CartItemsClassEntity
import com.example.applicationhome.core.domain.model.CartItemsDomainClass

fun CartItemsDomainClass.cartItemsDomainClassToCartItemsClass(): CartItemsClassEntity =
    CartItemsClassEntity(
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
        restaurantId = this.restaurantId
    )
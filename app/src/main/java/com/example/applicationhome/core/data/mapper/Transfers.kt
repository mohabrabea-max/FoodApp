package com.example.applicationhome.core.data.mapper

import com.example.applicationhome.core.data.local.entity.CartItemsClassEntity
import com.example.applicationhome.core.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.core.data.remote.dto.MealSizeDetailDto
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.CategoryEnum
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.MealSizeDetail
import com.example.applicationhome.core.domain.model.OrderItemsClass
import com.example.applicationhome.core.domain.model.OrderStates
import com.example.applicationhome.core.domain.model.OrderStatesEnum
import com.example.applicationhome.core.domain.model.OrderUiClass
import com.example.applicationhome.core.domain.model.SnackDomain
import com.example.applicationhome.core.ui.model.FoodItem

fun MealSizeDetailDto.mealSizeDetailDtoToMealSizeDetail(): MealSizeDetail =
    MealSizeDetail(
        size = this.size,
        price = this.price,
        snack = this.snack,
    )


fun OrdersDatabaseClass.ordersDatabaseClassToOrderUiClass(): OrderUiClass =
    OrderUiClass(
        orderId = this.orderId,
        userId = this.userId,
        date = this.date,
        state = OrderStates.fromEnum(OrderStatesEnum.fromString(this.state)),
        subtotal = this.subtotal,
        delivery = this.delivery,
        service = this.service,
        totalPrice = this.totalPrice,
        restaurantName = this.restaurantName,
        restaurantImage = this.restaurantImage,
        restaurantId = this.restaurantId,
        userInformation = this.userInformation,
        orderItems = this.orderItems,
        orderHistory = this.orderHistory,
        updatedAt = this.updatedAt
    )


fun OrderItemsClass.orderItemsClassToCartItemsClass(userId : String, resId : Int, image : String): CartItemsClassEntity =
    CartItemsClassEntity(
        userId = userId,
        mealKey = "${this.mealId}_${this.size}",
        mealId = this.mealId,
        name = this.mealName,
        type = this.type,
        size = this.size,
        quantity = this.quantity,
        priceOfOne = this.price,
        totalPrice = this.price * this.quantity,
        image = image,
        restaurantId = resId
    )

fun FoodItem.foodItemToCartItemsClass(userId : String, quantity : Int): CartItemsClassEntity =
    CartItemsClassEntity(
        userId = userId,
        mealKey = "${this.id}_${this.sizes.keys.last()}",
        mealId = this.id,
        name = this.name,
        type = CategoryEnum.SNACKS.rawValue,
        size = this.sizes.keys.last(),
        quantity = quantity,
        priceOfOne = this.sizes.values.last(),
        totalPrice = this.sizes.values.last() * quantity,
        image = this.image,
        restaurantId = this.restaurantId
    )

fun OrderItemsClass.orderItemsClassToCartItemsDomainClass(userId : String, resId : Int, image : String): CartItemsDomainClass =
    CartItemsDomainClass(
        userId = userId,
        mealKey = "${this.mealId}_${this.size}",
        mealId = this.mealId,
        name = this.mealName,
        type = this.type,
        size = this.size,
        quantity = this.quantity,
        priceOfOne = this.price,
        totalPrice = this.price * this.quantity,
        image = image,
        restaurantId = resId
    )

fun MealDomain.mealDomainToCartItemsDomainClass(userId : String, size : String, quantity : Int = 0): CartItemsDomainClass{
    val price = this.sizeOptions.find { it.size == size }?.price?: 0.0

    return CartItemsDomainClass(
        userId = userId,
        mealKey = "${this.id}_${size}",
        mealId = this.id,
        name = this.name,
        type = this.category,
        size = size,
        quantity = quantity,
        priceOfOne = price,
        totalPrice = price * quantity,
        image = this.image,
        restaurantId = this.restaurantId,
        discount = this.discount
    )
}

fun SnackDomain.snackDomainToCartItemsDomainClass(userId : String, size : String, quantity : Int = 0): CartItemsDomainClass{
    val price = this.priceANDsize[size]?: 0.0

    return CartItemsDomainClass(
        userId = userId,
        mealKey = "${this.id}_${size}",
        mealId = this.id,
        name = this.name,
        type = CategoryEnum.SNACKS.rawValue,
        size = size,
        quantity = quantity,
        priceOfOne = price,
        totalPrice = price * quantity,
        image = this.image,
        restaurantId = this.restaurantId,
        discount = this.discount
    )
}

package com.example.applicationhome.core.domain.model

import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.OrderStates
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.model.Snack
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SnacksEntity
import com.example.applicationhome.data.local.entity.UserClass

fun FoodItem.foodItemToMealsEntity(): MealsEntity =
    MealsEntity(
        this.id,
        this.category,
        this.name,
        this.details,
        this.image,
        this.sizeOptions,
        this.restaurantId,
        this.review
    )


fun Snack.snackToSnacksEntity(): SnacksEntity =
    SnacksEntity(
        this.id,
        this.name,
        this.details,
        this.image,
        this.priceANDsize,
        this.restaurantId,
        this.review
    )


fun SnacksEntity.snacksEntityToCartItemsClass(userId : String, quantity : Int): CartItemsClass =
    CartItemsClass(
        userId,
        "${this.id}_${this.priceANDsize.keys.last()}",
        this.id,
        this.name,
        "Snack",
        this.priceANDsize.keys.last(),
        quantity,
        this.priceANDsize.values.last(),
        this.priceANDsize.values.last() * quantity,
        this.image,
        this.restaurantId
    )


fun Restaurants.restaurantsToRestaurantsEntity(): RestaurantsEntity =
    RestaurantsEntity(
        this.id,
        this.name,
        this.typ.map { it.value },
        this.image,
        this.image2,
        this.review,
        this.background,
        this.searchKeywords,
        this.topFiveMeals
    )

fun UserClassFireBase.userClassFireBaseToUserDataDatabase(userData : String): UserClass =
    UserClass(
        id = userData,
        firstname = this.firstname,
        lastname = this.lastname,
        email = this.email,
        phonenumber = this.phonenumber,
        birthday = this.birthday,
        governorate = this.governorate,
        city = this.city,
        address = this.address,
        isActive = true
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
        orderHistory = this.orderHistory
    )


fun OrderItemsClass.orderItemsClassToCartItemsClass(userId : String, resId : Int, image : String): CartItemsClass =
    CartItemsClass(
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
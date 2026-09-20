package com.example.applicationhome.core.data.mapper

import com.example.applicationhome.core.data.local.entity.AddressesEntity
import com.example.applicationhome.core.data.local.entity.DiscountsEntity
import com.example.applicationhome.core.data.local.entity.MealsEntity
import com.example.applicationhome.core.data.local.entity.RestaurantsEntity
import com.example.applicationhome.core.data.local.entity.SnacksEntity
import com.example.applicationhome.core.data.local.entity.UserClass
import com.example.applicationhome.core.data.remote.dto.Discounts
import com.example.applicationhome.core.data.remote.dto.Meal
import com.example.applicationhome.core.data.remote.dto.Restaurants
import com.example.applicationhome.core.data.remote.dto.Snack
import com.example.applicationhome.core.domain.model.Address
import com.example.applicationhome.core.domain.model.UserClassFireBase

fun Meal.foodItemToMealsEntity(): MealsEntity =
    MealsEntity(
        id = this.id,
        category = this.category,
        name = this.name,
        details = this.details,
        image = this.image,
        sizeOptions = this.sizeOptions.map { it.mealSizeDetailDtoToMealSizeDetail() },
        restaurantId = this.restaurantId,
        review = this.review
    )

fun Snack.snackToSnacksEntity(): SnacksEntity =
    SnacksEntity(
        id = this.id,
        name = this.name,
        details = this.details,
        image = this.image,
        priceANDsize = this.priceANDsize,
        restaurantId = this.restaurantId,
        review = this.review
    )

fun Restaurants.restaurantsToRestaurantsEntity(): RestaurantsEntity =
    RestaurantsEntity(
        id = this.id,
        name = this.name,
        typ = this.typ.map { it.value },
        image = this.image,
        image2 = this.image2,
        review = this.review,
        background = this.background,
        searchKeywords = this.searchKeywords,
        topFiveMeals = this.topFiveMeals
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

fun Address.addressToAddressesEntity(userId : String, addressId : Long): AddressesEntity =
    AddressesEntity(
        addressId = addressId,
        userId = userId,
        title = this.title,
        house = this.house,
        street = this.street,
        phoneNumber = this.phoneNumber,
        additionalDirectionsState = this.additionalDirectionsState,
        addressLabelState = this.addressLabelState,
        latLocation = this.latLocation,
        lngLocation = this.lngLocation,
        locationName = this.locationName,
        locationFullName = this.locationFullName,
        lastUse = this.lastUse
    )

fun Discounts.discountsToDiscountsEntity(): DiscountsEntity =
    DiscountsEntity(
        discount = this.discount,
        mealId = this.mealId,
        restaurantId = this.restaurantId,
        startDiscount = this.startDiscount,
        endDiscount = this.endDiscount
    )
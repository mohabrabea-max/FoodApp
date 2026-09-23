package com.example.applicationhome.core.data.mapper

import com.example.applicationhome.core.data.local.entity.AddressesEntity
import com.example.applicationhome.core.data.local.entity.DiscountsEntity
import com.example.applicationhome.core.data.local.entity.MealsEntity
import com.example.applicationhome.core.data.local.entity.RestaurantsEntity
import com.example.applicationhome.core.data.local.entity.ReviewsEntity
import com.example.applicationhome.core.data.local.entity.ReviewsStarsEntity
import com.example.applicationhome.core.data.local.entity.SnacksEntity
import com.example.applicationhome.core.data.local.entity.UserClass
import com.example.applicationhome.core.data.remote.dto.Discounts
import com.example.applicationhome.core.data.remote.dto.Meal
import com.example.applicationhome.core.data.remote.dto.Restaurants
import com.example.applicationhome.core.data.remote.dto.ReviewsForGet
import com.example.applicationhome.core.data.remote.dto.ReviewsStars
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


fun ReviewsForGet.reviewsForGetToReviewsEntity(userId : String): ReviewsEntity =
    ReviewsEntity(
        userId = userId,
        userName = this.userName,
        createdAt = this.createdAt,
        resId = this.resId,
        stars = this.stars,
        comment = this.comment
    )

fun ReviewsStars.reviewsStarsToReviewsStarsEntity(): ReviewsStarsEntity =
    ReviewsStarsEntity(
        resId = this.resId,
        stars = this.stars,
        number = this.number
    )
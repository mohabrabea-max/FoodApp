package com.example.applicationhome.core.data.mapper

import com.example.applicationhome.core.data.local.entity.CartItemsClassEntity
import com.example.applicationhome.core.data.local.entity.DiscountsEntity
import com.example.applicationhome.core.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.core.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.core.data.local.entity.ReviewsEntity
import com.example.applicationhome.core.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.DiscountsDomainClass
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.RestaurantDomainClass
import com.example.applicationhome.core.domain.model.ReviewsDomainClass
import com.example.applicationhome.core.domain.model.SnackDomain

fun MealWithFavoriteStatus.mealWithFavoriteStatusToMealDomain(): MealDomain =
    MealDomain(
        id = this.meal.id,
        category = this.meal.category,
        name = this.meal.name,
        details = this.meal.details,
        image = this.meal.image,
        sizeOptions = this.meal.sizeOptions,
        restaurantId = this.meal.restaurantId,
        review = this.meal.review,
        discount = this.discount?.discountsEntityToDiscountsDomainClass(),
        isFavorite = this.favoriteInfo != null && !this.favoriteInfo.isDeletedOffline
    )

fun SnackWithFavoriteStatus.snackWithFavoriteStatusToSnackDomain(): SnackDomain =
    SnackDomain(
        id = this.snack.id,
        name = this.snack.name,
        details = this.snack.details,
        image = this.snack.image,
        priceANDsize = this.snack.priceANDsize,
        restaurantId = this.snack.restaurantId,
        review = this.snack.review,
        discount = this.discount?.discountsEntityToDiscountsDomainClass(),
        isFavorite = this.favoriteInfo != null && !this.favoriteInfo.isDeletedOffline
    )

fun RestaurantWithFavoriteStatus.restaurantWithFavoriteStatusToRestaurantDomainClass(): RestaurantDomainClass =
    RestaurantDomainClass(
        id = this.restaurant.id,
        typ = this.restaurant.typ,
        categories = this.categories.map { it.name },
        name = this.restaurant.name,
        image = this.restaurant.image,
        image2 = this.restaurant.image2,
        review = Pair(this.stars?.stars?: 0.0, this.stars?.number?: 0),
        background = this.restaurant.background,
        searchKeywords = this.restaurant.searchKeywords,
        topFiveMeals = this.restaurant.topFiveMeals,
        isFavorite = this.favoriteInfo != null && !this.favoriteInfo.isDeletedOffline,
        discounts = this.discounts?.map { it.discountsEntityToDiscountsDomainClass() }
    )

fun DiscountsEntity.discountsEntityToDiscountsDomainClass(): DiscountsDomainClass =
    DiscountsDomainClass(
        discount = this.discount,
        mealId = this.mealId,
        restaurantId = this.restaurantId,
        startDiscount = this.startDiscount,
        endDiscount = this.endDiscount
    )

fun CartItemsClassEntity.cartItemsClassEntityToCartItemsDomainClass(discounts : DiscountsEntity?): CartItemsDomainClass =
    CartItemsDomainClass(
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
        discount = discounts?.discountsEntityToDiscountsDomainClass()
    )

fun ReviewsEntity.reviewsEntityToReviewsDomainClass(): ReviewsDomainClass =
    ReviewsDomainClass(
        userId = this.userId,
        userName = this.userName,
        createdAt = this.createdAt,
        resId = this.resId,
        stars = this.stars,
        comment = this.comment
    )
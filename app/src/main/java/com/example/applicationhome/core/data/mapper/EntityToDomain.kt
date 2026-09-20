package com.example.applicationhome.core.data.mapper

import com.example.applicationhome.core.data.local.entity.CartItemsClassEntity
import com.example.applicationhome.core.data.local.entity.DiscountsEntity
import com.example.applicationhome.core.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.core.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.DiscountsDomainClass
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.SnackDomain
import com.example.applicationhome.core.ui.model.DiscountsUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        isFavorite = this.favoriteInfo != null
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
        isFavorite = this.favoriteInfo != null
    )

fun DiscountsDomainClass.discountsDomainClassToDiscountsUi(): DiscountsUi? {
    val isDiscountExpired = System.currentTimeMillis() < this.endDiscount

    return if(isDiscountExpired){
        DiscountsUi(
            discount = this.discount,
            endDiscount = formatDate(this.endDiscount)
        )
    }else{
        null
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(date)
}

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
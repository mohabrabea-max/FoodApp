package com.example.applicationhome.core.ui.mapper

import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.DiscountsDomainClass
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.RestaurantDomainClass
import com.example.applicationhome.core.domain.model.ReviewsDomainClass
import com.example.applicationhome.core.domain.model.SnackDomain
import com.example.applicationhome.core.ui.model.CartItemsUIClass
import com.example.applicationhome.core.ui.model.DiscountsUI
import com.example.applicationhome.core.ui.model.FoodItem
import com.example.applicationhome.core.ui.model.RestaurantsUIClass
import com.example.applicationhome.core.ui.model.ReviewsUIClass
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun CartItemsDomainClass.cartItemsDomainClassToCartItemsUiClass(): CartItemsUIClass  =
    CartItemsUIClass(
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


private fun Int.toDynamicFormattedString(): String {
    return when {
        this >= 1_000_000 -> "(+${this / 1_000_000}M)"
        this >= 1_000 -> "(+${this / 1_000}k)"
        else -> "($this)"
    }
}
fun RestaurantDomainClass.restaurantDomainClassToRestaurantsUiClass(): RestaurantsUIClass {
    val number = this.review.second.toDynamicFormattedString()
    val finalReview = Pair(this.review.first, number)

    return RestaurantsUIClass(
        id = this.id,
        typ = this.typ,
        categories = this.categories,
        name = this.name,
        image = this.image,
        image2 = this.image2,
        review = finalReview ,
        background = this.background,
        searchKeywords = this.searchKeywords,
        topFiveMeals = this.topFiveMeals,
        isFavorite = this.isFavorite,
        discounts = this.discounts?.map { it?.discountsDomainClassToDiscountsUi() }
    )
}


fun DiscountsDomainClass.discountsDomainClassToDiscountsUi(): DiscountsUI? {
    val isDiscountExpired = System.currentTimeMillis() < this.endDiscount

    return if(isDiscountExpired){
        DiscountsUI(
            discount = this.discount,
            endDiscount = formatDate(this.endDiscount)
        )
    }else{
        null
    }
}


fun ReviewsDomainClass.reviewsDomainClassToReviewsUIClass(): ReviewsUIClass =
    ReviewsUIClass(
        userId = this.userId,
        userName = this.userName,
        createdAt = formatDate(this.createdAt),
        resId = this.resId,
        stars = this.stars,
        comment = this.comment
    )

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(date)
}
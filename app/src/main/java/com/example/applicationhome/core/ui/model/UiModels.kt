package com.example.applicationhome.core.ui.model

import androidx.annotation.Keep
import com.example.applicationhome.core.domain.model.CategoriesInWithTitle
import com.example.applicationhome.core.domain.model.MealSizeDetail

sealed interface UiStates {
    data object Loading : UiStates
    //data object Empty : UiStates
    data object Success : UiStates
    data object Offline : UiStates
}

sealed interface UserUiState {
    data object Starting : UserUiState
    data object GuestMode : UserUiState
    data object Success : UserUiState
    data object Offline : UserUiState
}

@Keep
data class DiscountsUI(
    val discount : Int = 0,
    val endDiscount : String = ""
)

sealed interface FoodItem {
    val id : Int
    val name : String
    val details : String
    val image : String
    val sizes : Map<String, Double>
    val restaurantId : Int
    val review : Double
    val discount : DiscountsUI?
    val isFavorite : Boolean

    data class MealItem(
        override val id : Int = 0,
        override val name : String = "",
        override val details : String = "",
        override val image : String = "",
        override val sizes : Map<String, Double> = mapOf("" to 0.0),
        override val restaurantId : Int = 0,
        override val review : Double = 0.0,
        override val discount : DiscountsUI? = null,
        override val isFavorite : Boolean = false,
        val category : String = "ALL",
        val sizeOptions : List<MealSizeDetail>
    ) : FoodItem

    data class SnackItem(
        override val id : Int = 0,
        override val name : String = "",
        override val details : String = "",
        override val image : String = "",
        override val sizes : Map<String, Double> = mapOf("" to 0.0),
        override val restaurantId : Int = 0,
        override val review : Double = 0.0,
        override val discount : DiscountsUI? = null,
        override val isFavorite : Boolean = false
    ) : FoodItem
}


data class CartItemsUIClass(
    val userId : String = "",
    val mealKey : String = "",
    val mealId : Int = 0,
    val name : String = "",
    val type : String = "",
    val size : String = "",
    val quantity: Int = 0,
    val priceOfOne : Double = 0.0,
    val totalPrice : Double = 0.0,
    val image : String = "",
    val restaurantId : Int = 0,
    val discount : DiscountsUI? = null,
    val finalPrice : Double = 0.0
)


data class RestaurantsUIClass(
    val id : Int = 0,
    val typ : List<CategoriesInWithTitle> = emptyList(),
    val categories : List<String> = emptyList(),
    val name : String = "",
    val image : String = "",
    val image2 : String = "",
    val review : String = "",
    val background : String = "",
    val searchKeywords: String = "",
    val topFiveMeals : String = "",
    val isFavorite : Boolean = false,
    val discounts : List<DiscountsUI?>? = null
)

data class ReviewsUIClass(
    val userId : String = "",
    val userName : String = "",
    val createdAt : String = "",
    val resId : Int = 0,
    val stars : Double = 0.0,
    val comment : String = ""
)
package com.example.applicationhome.core.domain.model

import androidx.annotation.StringRes
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.model.FoodItem
import com.example.applicationhome.core.ui.model.RestaurantsUIClass


data class MealDomain(
    val id: Int = 0,
    val category: String = "ALL",
    val name: String = "",
    val details: String = "",
    val image: String = "",
    val sizeOptions: List<MealSizeDetail> = listOf(),
    val restaurantId: Int = 0,
    val review: Double = 0.0,
    val discount: DiscountsDomainClass? = null,
    val isFavorite: Boolean = false
)

data class SnackDomain(
    val id: Int = 0,
    val name: String = "",
    val details: String = "",
    val image: String = "",
    val priceANDsize: Map<String, Double> = emptyMap(),
    val restaurantId: Int = 0,
    val review: Double = 0.0,
    val discount: DiscountsDomainClass? = null,
    val isFavorite: Boolean = false
)

data class DiscountsDomainClass(
    val discount : Int = 0,
    val mealId : Int = 0,
    val restaurantId : Int = 0,
    val startDiscount : Long = 0L,
    val endDiscount : Long = 0L
)

data class MealSizeDetail(
    val size : String = "",
    val price : Double = 0.0,
    val snack : Map<String, MealSnacks> = emptyMap()
)

data class MealSnacks(
    val size : String = "",
    val name : String = "",
    val details : String = "",
    val image : String = ""
)

data class RestaurantDomainClass(
    val id : Int = 0,
    val typ : List<CategoriesInWithTitle> = emptyList(),
    val categories : List<String> = emptyList(),
    val name : String = "",
    val image : String = "",
    val image2 : String = "",
    val review : Pair<Double, Int> = Pair(0.0, 0),
    val background : String = "",
    val searchKeywords: String = "",
    val topFiveMeals : String = "",
    val isFavorite : Boolean = false,
    val discounts : List<DiscountsDomainClass?>? = null
)


data class RestaurantUiState(
    val restaurantData : RestaurantsUIClass = RestaurantsUIClass(),
    val bottomSheetItem : FoodItem? = null

)

data class BottomSheetActions(
    val navigation : (Screens) -> Unit,
    val addFavorite : () -> Unit,
    val removeFavorite : () -> Unit,
    val selectSize : (String) -> Unit,
    val updateCount : (
        food : FoodItem,
        size : String,
        newCount : Int
    ) -> Unit,
    val clearAndStartNewCart : (Int) -> Unit,
    val minusnewCount : () -> Unit,
    val plusnewCount : () -> Unit,
    val deletenewCount : () -> Unit,
    val alertDialogFalse : () -> Unit,
    val closeBottomSheet : () -> Unit
)

sealed interface ShowSnackBarEvent {
    data class AddedToFavorite(
        val message : String,
        val actionLabel : String = "",
        val action : () -> Unit = {}
    ) : ShowSnackBarEvent

    data class RemoveFromFavorite(
        val message : String,
        val actionLabel : String = "",
        val undo : () -> Unit = {}
    ) : ShowSnackBarEvent


    data class AddedToCart(
        val message : String,
        val actionLabel : String = "",
        val action : () -> Unit = {}
    ) : ShowSnackBarEvent

    data class RemoveFromCart(
        val message : String,
        val actionLabel : String = "",
        val undo : () -> Unit = {}
    ) : ShowSnackBarEvent
}

sealed interface AddToCartStates {
    data object Idle : AddToCartStates
    data object Success : AddToCartStates
    data class ErrorInLoginState(
        @StringRes val title : Int = R.string.sign_in_required,
        @StringRes val message : Int = R.string.please_sign_in_or_create_an_account_to_add_items_to_your_cart_and_proceed_with_your_order
    ) : AddToCartStates
    data class ErrorInCartRestaurant(
        @StringRes val title : Int = R.string.start_a_new_cart,
        val restaurantName : String = "",
        @StringRes val message : Int = R.string.a_new_order_will_clear_your_cart_with,
        val food : CartItemsDomainClass = CartItemsDomainClass(),
        val size : String = ""
    ) : AddToCartStates
}

enum class CategoryEnum(val rawValue : String){
    BURGER("BURGER"),
    PIZZA("PIZZA"),
    CHICKEN("CHICKEN"),
    KOSHARY("KOSHARY"),
    GRILL("GRILL"),
    SNACKS("SNACKS"),
    DRINK("DRINK"),
    NOTHING("NOTHING");

    companion object {
        fun fromString(value : String?): CategoryEnum {
            return entries.find { it.rawValue.equals(value, ignoreCase = true) } ?: NOTHING
        }
    }
}

data class CategoriesInWithTitle(
    val title : String = "",
    val category : String = "",
    val index : Int = 0
)

sealed interface CategoryInterface {

    data object Burgers : CategoryInterface
    data object Chicken : CategoryInterface
    data object Pizza : CategoryInterface
    data object Koshary : CategoryInterface
    data object Grill : CategoryInterface

    data object Snacks : CategoryInterface
    data object Drinks : CategoryInterface

    data object Custom : CategoryInterface
}


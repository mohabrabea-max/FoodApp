package com.example.applicationhome.data.data.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.paging.compose.LazyPagingItems
import com.example.applicationhome.R
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.data.local.entity.UserClass
import com.google.gson.annotations.SerializedName

@Keep
data class FoodItem(
    val id : Int = 0,
    val category : String = "ALL",
    val name : String = "",
    val details : String = "",
    val image : String = "",
    @SerializedName("sizes")
    val sizeOptions : List<MealSizeDetail> = listOf(),
    var restaurantId : Int = 0,
    @SerializedName("rating")
    val review : Double = 0.0,
    val updatedAt : Long = 0L
)

@Keep
data class Snack(
    val id : Int = 0,
    val name : String = "",
    val details : String = "",
    val image : String = "",
    @SerializedName("prices")
    val priceANDsize : Map<String, Double> = emptyMap(),
    var restaurantId : Int = 0,
    @SerializedName("rating")
    val review : Double = 0.0,
    val updatedAt : Long = 0L
)

@Keep
data class Drink(
    val id : Int,
    val name : String,
    val image : List<String>,
    val priceANDsize : Map<String, Double>,
    var restaurantId : Int,
    val updatedAt : Long = 0L
)

@Keep
data class MealSizeDetail(
    val size : String = "",
    val price : Double = 0.0,
    @SerializedName("details")
    val snack : Map<Int, MealSnacks> = emptyMap()
)

@Keep
data class MealSnacks(
    val size : String = "",
    val name : String = "",
    val image : String = ""
)

@Keep
data class Categories(
    val id : Int = 0,
    val name : String = "",
    val type : String = "ALL",
    val image : String = "",
    val icon : String = "",
    val updatedAt : Long = 0L
)

@Keep
data class Offers(
    val restaurantId : Int = 0,
    val id : Int = 0,
    @SerializedName("title")
    val name : String = "",
    val image : String = "",
    val updatedAt : Long = 0L
)

@Keep
data class Restaurants(
    val id : Int = 0,
    @SerializedName("types")
    val typ : Map<String, CategoriesInWithTitle> = emptyMap(),
    val categories : Map<Int, String> = emptyMap(),
    val name : String = "",
    @SerializedName("logo")
    val image : String = "",
    @SerializedName("main_image")
    val image2 : String = "",
    @SerializedName("rating")
    val review : Double = 0.0,
    val background : String = "",
    val searchKeywords: String = "",
    val topFiveMeals : String = "",
    val updatedAt : Long = 0L
)



@Keep
data class FavoriteClass(
    val id : Int,
    val typ : String,
    val restaurants : Int
)

sealed interface BottomSheetItem {
    val id : Int
    val name : String
    val details : String
    val image : String
    val sizes : Map<String, Double>
    val restaurantId : Int
    val review : Double
    val isFavorite : Boolean

    data class MealItem(val meal : MealWithFavoriteStatus?) : BottomSheetItem{
        override val id: Int
            get() = meal?.meal?.id ?: 0

        override val name: String
            get() = meal?.meal?.name ?: ""

        override val details: String
            get() = meal?.meal?.details ?: ""

        override val image: String
            get() = meal?.meal?.image ?: ""

        override val sizes: Map<String, Double>
            get() = meal?.meal?.sizeOptions?.associate { it.size to it.price } ?: emptyMap()

        override val restaurantId: Int
            get() = meal?.meal?.restaurantId ?: 0

        override val review: Double
            get() = meal?.meal?.review ?: 0.0

        override val isFavorite: Boolean
            get() = meal?.isFavorite ?: false
    }

    data class SnackItem(val snack : SnackWithFavoriteStatus?) : BottomSheetItem{
        override val id: Int
            get() = snack?.snack?.id ?: 0

        override val name: String
            get() = snack?.snack?.name ?: ""

        override val details: String
            get() = snack?.snack?.details ?: ""

        override val image: String
            get() = snack?.snack?.image ?: ""

        override val sizes: Map<String, Double>
            get() = snack?.snack?.priceANDsize ?: emptyMap()

        override val restaurantId: Int
            get() = snack?.snack?.restaurantId ?: 0

        override val review: Double
            get() = snack?.snack?.review ?: 0.0

        override val isFavorite: Boolean
            get() = snack?.isFavorite ?: false
    }
}

data class RestaurantUiState(
    val restaurantData : RestaurantWithFavoriteStatus = RestaurantWithFavoriteStatus(),
    val bottomSheetItem : BottomSheetItem? = null

)

data class BottomSheetActions(
    val navigation : (Screens) -> Unit,
    val addFavorite : () -> Unit,
    val removeFavorite : () -> Unit,
    val selectSize : (String) -> Unit,
    val updateCount : (
        food : CartItemsClass,
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
        val food : CartItemsClass = CartItemsClass(),
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

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Success : HomeUiState
    data object Offline : HomeUiState
}

sealed interface UserUiState {
    data object Starting : UserUiState
    data object GuestMode : UserUiState
    data object Success : UserUiState
    data object Offline : UserUiState
}


data class HomeScreenActions(
    val select : (CategoriesEntity) -> Unit = {},
    val unSelected : () -> Unit = {},
    val addRestaurantsFavorite : (FavoriteRestaurantEntity) -> Unit = {},
    val removeRestaurantsFavorite : (Int) -> Unit = {}
)

data class HomeScreenParameters(
    val isNetworkAvailable : Boolean = false,
    val categories : List<CategoriesEntity> = emptyList(),
    val categorySelected : Int = 0,
    val userData : UserClass = UserClass(),
    val restaurants : LazyPagingItems<RestaurantWithFavoriteStatus>? = null,
    val offers : List<OffersEntity> = emptyList(),
)
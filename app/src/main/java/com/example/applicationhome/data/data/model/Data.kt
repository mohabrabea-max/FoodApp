package com.example.applicationhome.data.data.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.google.gson.annotations.SerializedName

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

data class Drink(
    val id : Int,
    val name : String,
    val image : List<String>,
    val priceANDsize : Map<String, Double>,
    var restaurantId : Int,
    val updatedAt : Long = 0L
)

data class MealSizeDetail(
    val size : String = "",
    val price : Double = 0.0,
    @SerializedName("details")
    val snack : Map<Int, MealSnacks> = emptyMap()
)

data class MealSnacks(
    val size : String = "",
    val name : String = "",
    val image : String = ""
)

data class Categories(
    val id : Int = 0,
    val name : String = "",
    val type : String = "ALL",
    val image : String = "",
    val icon : String = "",
    val updatedAt : Long = 0L
)

data class Offers(
    val restaurantId : Int = 0,
    val id : Int = 0,
    @SerializedName("title")
    val name : String = "",
    val image : String = "",
    val updatedAt : Long = 0L
)

data class Restaurants(
    val id : Int = 0,
    @SerializedName("types")
    val typ : List<String> = listOf(),
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



data class Options(
    val title : String,
    val icon : ImageVector,
    val screen: String
)

data class AccountTextFieldClass(
    val id : Int,
    val title : String,
    val emptyCount : String,
    val textField : TextFieldState,
    val icon : ImageVector?
)

data class Settings(
    val title : String,
    val icon : ImageVector
)

data class ProfileOptions(
    val title : String,
    var description : String?,
    val icon: ImageVector,
    val screen : Screens
)


data class FavoriteClass(
    val id : Int,
    val typ : String,
    val restaurants : Int,
    val updatedAt : Long = 0L
)

data class UserClassFireBase(
    val firstname : String = "",
    val lastname : String = "",
    val email : String = "",
    val password : String = "",
    val phonenumber : String = "",
    val birthday : String = "",
    val governorate : String = "",
    val city : String = "",
    val address : String = ""
)

sealed interface ProfileEditResult {
    data object Success : ProfileEditResult
    data object DataIncomplete : ProfileEditResult
    data object PhoneNumberIncomplete : ProfileEditResult
    data object NetworkError : ProfileEditResult
}

data class City(
    val englishName: String,
    val arabicName: String
)

data class Governorate(
    val name: String,
    val cities: List<City>
)

data class FirebasePostResponse(val name : String)

data class TextFieldClassFromConfirmOrderScreen(
    val textField : TextFieldState,
    val title : String
)

data class MapUiState(
    val latitude : Double = 30.0444,
    val longitude : Double = 31.2357,
    val locationName : String = "",
    val locationFullName : String = "",
    val isLoading : Boolean = false,
)

enum class PaymentMethod(
    val title : String,
    val icon : ImageVector,
    val integrationId : Int
){
    CARD(
        title = "Bank card (Visa / Mastercard)",
        icon = Icons.Default.Payment,
        integrationId = 111111
    ),

    WALLET(
        title = "E-wallet(Vodafone Cash)",
        icon = Icons.Default.AccountBalanceWallet,
        integrationId = 111111
    ),

    CASH(
        title = "Cash on delivery",
        icon = Icons.Default.Payments,
        integrationId = 0
    )

}

sealed interface PaymentState{
    data object Idle : PaymentState
    data object Loading : PaymentState
    data object Success : PaymentState
    data object Failed : PaymentState
}

data class CheckoutUiState(
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH,
    val isProcessing: Boolean = false
)

data class OrderItemsClass(
    val mealId : Int = 0,
    val mealName : String = "",
    val size : String = "",
    val price : Double = 0.0,
    val quantity : Int = 0,
    val image : String = "",
    val type : String = ""
)

data class UserInformationInOrderClass(
    val name : String = "",
    val phonenumber : String = "",
    val address : String = "",
    val location : String = "",
    val locationAddress : String = ""
)

data class OrdersClass(
    val date : String = "",
    val state : String = "",
    val subtotal : Double = 0.0,
    val delivery : Double = 0.0,
    val service : Double = 0.0,
    val totalPrice : Double = 0.0,
    val userInformation : UserInformationInOrderClass = UserInformationInOrderClass(),
    val orderItems : List<OrderItemsClass> = emptyList(),
    val restaurantName : String = "",
    val restaurantImage : String = "",
    val restaurantId : Int = 0
)

sealed interface ActionsStates{
    data object Idle : ActionsStates
    data object Loading : ActionsStates
    data object Success : ActionsStates
    data class Failed(val error : String) : ActionsStates
}

sealed interface UiEvent {
    object ShowNetworkError : UiEvent
}

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
    val updateCount : (food : CartItemsClass, size : String, newCount : Int) -> Unit,
    val clearAndStartNewCart : (Int) -> Unit,
    val minusnewCount : () -> Unit,
    val plusnewCount : () -> Unit,
    val deletenewCount : () -> Unit,
    val alertDialogFalse : () -> Unit,
    val closeBottomSheet : () -> Unit
)
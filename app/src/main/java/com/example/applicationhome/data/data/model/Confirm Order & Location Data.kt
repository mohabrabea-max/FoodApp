package com.example.applicationhome.data.data.model

import androidx.annotation.StringRes
import androidx.compose.foundation.text.input.TextFieldState


data class ConfirmOrderUiState(
    val streetAndHome : Pair<String, String> = Pair("", ""),
    val confirmOrderError : ProfileEditResult? = null,
    val locationState : MapUiState = MapUiState(),
    val locationImage : String = "",
    val confirmOrderState : ActionsStates = ActionsStates.Idle,
    val payMethodState : CheckoutUiState = CheckoutUiState(),
    val paymentState : PaymentState = PaymentState.Idle,
    val paymentApiState : PaymentApiState = PaymentApiState.Idle,
    val isSavePhoneNumberSelected : Boolean = false,
    val isSaveAddressSelected : Boolean = false,
    val bottonState : Boolean = false,
    val isButtonClicked : Boolean = false
)

data class CheckoutFormState(
    val addressTitle : TextFieldState = TextFieldState(),
    val houseState : TextFieldState = TextFieldState(),
    val streetState : TextFieldState = TextFieldState(),
    val phoneNumberState : TextFieldState = TextFieldState(),
    val additionalDirectionsState : TextFieldState = TextFieldState(),
    val addressLabelState : TextFieldState = TextFieldState()
)

enum class ConfirmOrderScreenTextFieldEnum {
    TITLE,
    HOUSE,
    STREET,
    PHONE,
    PHONE_WITHOUT_BUTTON,
    ADDITIONAL,
    ADDRESS
}

data class TextFieldClassFromConfirmOrderScreen(
    val textField : TextFieldState,
    @StringRes val title : Int,
    val type : ConfirmOrderScreenTextFieldEnum
)

data class MapUiState(
    val latitude : Double = 30.0444,
    val longitude : Double = 31.2357,
    val locationName : String = "",
    val locationFullName : String = "",
    val isLoading : Boolean = false,
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
    val additionalDirectionsState : String = "",
    val addressLabelState : String = "",
    val latLocation : String = "",
    val lngLocation : String = "",
    val locationAddress : String = ""
)

data class OrderHistoryClass(
    val date : String,
    val state : String,
    val details : String
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
    val orderHistory : List<OrderHistoryClass>,
    val restaurantName : String = "",
    val restaurantImage : String = "",
    val restaurantId : Int = 0,
    val updatedAt : Long = 0L
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

sealed interface MapEntryPoint {
    data object Initial : MapEntryPoint
    data object UserData : MapEntryPoint
    data object Checkout : MapEntryPoint
}

sealed class ConfirmOrderScreens(val index : Int) {
    data object SelectAddress : ConfirmOrderScreens(0)
    data class Map(val entryPoint : MapEntryPoint = MapEntryPoint.Initial) : ConfirmOrderScreens(
        when(entryPoint){
            MapEntryPoint.Initial -> { 1 }
            else -> { 5 }
        }
    )
    data object UserData : ConfirmOrderScreens(2)
    data object Checkout : ConfirmOrderScreens(3)
    data object PaymentGateway : ConfirmOrderScreens(4)
}


data class Address(
    val title : String = "",
    val house : String = "",
    val street : String = "",
    val phoneNumber : String = "",
    val additionalDirectionsState : String = "",
    val addressLabelState : String = "",
    val latLocation : String = "",
    val lngLocation : String = "",
    val locationName : String = "",
    val locationFullName : String = "",
    val lastUse : Long = 0L
)

sealed interface EditAddressModeState {
    data object Edit : EditAddressModeState
    data object ReadOnly : EditAddressModeState
}

sealed class LocationsScreens(val index : Int){
    data object Locations : LocationsScreens(0)
    data class ViewAddressInformation(
        val state : EditAddressModeState = EditAddressModeState.ReadOnly
    ) : LocationsScreens(2)
    data class Map(val entryPoint : MapEntryPoint = MapEntryPoint.Initial) : LocationsScreens(
        when(entryPoint){
            MapEntryPoint.Initial -> { 1 }
            else -> { 3 }
        }
    )
}

data class AddressesUiState(
    val streetAndHome : Pair<String, String> = Pair("", ""),
    val confirmOrderError : ProfileEditResult? = null,
    val locationState : MapUiState = MapUiState(),
    val locationImage : String = "",
    val confirmOrderState : ActionsStates = ActionsStates.Idle,
    val bottonState : Boolean = false,
    val isButtonClicked : Boolean = false
)
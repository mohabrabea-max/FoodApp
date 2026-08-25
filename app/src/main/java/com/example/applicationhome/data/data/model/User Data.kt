package com.example.applicationhome.data.data.model

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

data class AccountTextFieldClass(
    val id : Int,
    val title : String,
    val emptyCount : String,
    val textField : TextFieldState,
    val icon : ImageVector?
)

data class ProfileOptions(
    val title : String,
    var description : String?,
    val icon: ImageVector,
    val screen: String
)

data class UserClassFireBase(
    val firstname : String = "",
    val lastname : String = "",
    val email : String = "",
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


sealed class SignUpScreens(val index : Int) {
    data object BasicDataScreen : SignUpScreens(1)
    data object OptionalDataScreen : SignUpScreens(2)
}

sealed interface LoginStates {
    data object Success : LoginStates
    data class Error(val errorMessage : String) : LoginStates
}

sealed interface ChickEmailStates {
    data object Success : ChickEmailStates
    data object EmailIsNotTrue : ChickEmailStates
    data class NetworkError(val errorMessage : String) : ChickEmailStates
}

sealed interface SignUpStates {
    data class Success(val userId : String) : SignUpStates
    data class Error(val errorMessage : String) : SignUpStates
}

data class LoginTextFields(
    val title : String,
    val textField : TextFieldState,
    val icon : ImageVector,
    val type : TextFieldsTypes
)

enum class ErrorsType{
    NETWORK,
    DATA,
    UNKNOWNERROR
}

data class SignUpBasicTextFields(
    val title : String,
    val textField : TextFieldState,
    val errorMessage : String? = null,
    val icon : ImageVector,
    val type : TextFieldsTypes
)

data class SignUpFullNameTextFields(
    val title : String,
    val textField : TextFieldState,
    val errorMessage : Boolean = false,
    val roundedCornerShape : RoundedCornerShape,
    val startPadding : Dp,
    val endPadding : Dp
)

data class VerificationTextFields(
    val title : String,
    val textField : TextFieldState,
    val error : Boolean = false,
    val stateColor : Color
)

sealed interface TextFieldsTypes {
    data object Basic : TextFieldsTypes
    data object Password : TextFieldsTypes
    data object PhonNumber : TextFieldsTypes
}

sealed class LoginPages(val index : Int) {
    data object EmailPage : LoginPages(1)
    data object VerificationCodePage : LoginPages(2)
    data object ChangePasswordPage : LoginPages(3)
}

sealed interface AuthError {
    data object NetworkError : AuthError
    data object EmailAlreadyExists : AuthError
    data object TooManyRequests : AuthError
    data class UnknownError(val message: String) : AuthError
}

sealed interface SignUpErrors {
    data class Error(val message : String) : SignUpErrors
}
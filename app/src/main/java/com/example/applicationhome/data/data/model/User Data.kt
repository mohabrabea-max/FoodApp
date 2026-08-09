package com.example.applicationhome.data.data.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.vector.ImageVector

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
    val screen : Screens
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

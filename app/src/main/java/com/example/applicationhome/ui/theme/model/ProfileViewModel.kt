package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.UserClass
import com.example.applicationhome.data.data.model.AccountTextFieldClass
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.ProfileSelection
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.data.repository.ProfileRepository
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository : ProfileRepository
) : ViewModel(){

    //       *** ---------------------------- \\*** Edite Profile  ***// ---------------------------- ***

    val userData = userRepository.userData

    val firstNameTextField = TextFieldState()
    val lastNameTextField = TextFieldState()
    val phoneNumberTextField = TextFieldState()
    val addressTextField = TextFieldState()

    val profileTextFields  = listOf(
        AccountTextFieldClass(
            102,
            "First Name",
            "First Name",
            firstNameTextField,
            Icons.Default.Edit
        ),
        AccountTextFieldClass(
            103,
            "Last Name",
            "Last Name",
            lastNameTextField,
            Icons.Default.Edit
        ),
        AccountTextFieldClass(
            104,
            "Phone number",
            "01011223344",
            phoneNumberTextField,
            Icons.Default.Edit
        ),
        AccountTextFieldClass(
            105,
            "Address",
            "street - building - floor",
            addressTextField,
            Icons.Default.Edit
        )
    )


    val selectedDate = MutableStateFlow("")

    fun selectDate(date : String){
        selectedDate.value = date
    }


    val selectedGovernorate = MutableStateFlow("")

    val selectedCity = MutableStateFlow("")

    val profileSelection = listOf(
        ProfileSelection(
            107,
            "Governorate",
            "Governorate",
            Icons.Default.Add
        ),
        ProfileSelection(
            108,
            "City",
            "City",
            Icons.Default.Add
        )
    )

    val isButtonClicked = MutableStateFlow(false)

    val isDataEdited = MutableStateFlow(false)


    init{
        viewModelScope.launch {
            userData.collect { user ->
                firstNameTextField.edit {
                    replace(0, length, user.firstname)
                }

                lastNameTextField.edit {
                    replace(0, length, user.lastname)
                }

                phoneNumberTextField.edit {
                    replace(0, length, user.phonenumber)
                }

                addressTextField.edit {
                    replace(0, length, user.address)
                }

                selectedDate.value = user.birthday
            }
        }
    }


    private fun buttonClick(){
        isButtonClicked.value = true
    }

    fun isDataChanged(){
        val userDataDatabase = UserClass(
            userData.value.id,
            firstNameTextField.text.toString(),
            lastNameTextField.text.toString(),
            userData.value.email,
            userData.value.password,
            phoneNumberTextField.text.toString(),
            selectedDate.value,
            selectedGovernorate.value,
            selectedCity.value,
            addressTextField.text.toString(),
            true
        )

        isDataEdited.value = userData.value != userDataDatabase
    }

    fun editeProfile(): ProfileEditResult {
        if(
            firstNameTextField.text.isEmpty() ||
            lastNameTextField.text.isEmpty()
        ){
            buttonClick()
            return ProfileEditResult.DataIncomplete
        }

        val validPrefixes = listOf("010", "011", "012", "015")
        if(
            (
                phoneNumberTextField.text.length != 11
                || !validPrefixes.any { phoneNumberTextField.text.startsWith(it) }
            )
            && phoneNumberTextField.text.isNotEmpty()
        ) return ProfileEditResult.PhoneNumberIncomplete

        viewModelScope.launch {
            val userDataFireBase = UserClassFireBase(
                firstNameTextField.text.toString(),
                lastNameTextField.text.toString(),
                userData.value.email,
                userData.value.password,
                phoneNumberTextField.text.toString(),
                selectedDate.value,
                selectedGovernorate.value,
                selectedCity.value,
                addressTextField.text.toString()
            )

            val userDataDatabase = UserClass(
                userData.value.id,
                firstNameTextField.text.toString(),
                lastNameTextField.text.toString(),
                userData.value.email,
                userData.value.password,
                phoneNumberTextField.text.toString(),
                selectedDate.value,
                selectedGovernorate.value,
                selectedCity.value,
                addressTextField.text.toString(),
                true
            )

            profileRepository.editeProfile(userData.value.id, userDataFireBase, userDataDatabase)
            isDataEdited.value = false
        }
        return ProfileEditResult.Success
    }
}
package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.UserClass
import com.example.applicationhome.data.data.model.AccountTextFieldClass
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

    val profileSelection = listOf(
        ProfileSelection(
            106,
            "Birthday",
            "1 / 1 / 2000",
            Icons.Default.Add
        ),
        ProfileSelection(
            107,
            "Country",
            "Country",
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
            addressTextField.text.toString(),
            true
        )

        isDataEdited.value = userData.value != userDataDatabase
    }

    fun editeProfile(): String{
        if(
            firstNameTextField.text.isEmpty() ||
            lastNameTextField.text.isEmpty()
        ){
            buttonClick()
            return "Data Incomplete"
        }

        viewModelScope.launch {

            val userDataFireBase = UserClassFireBase(
                firstNameTextField.text.toString(),
                lastNameTextField.text.toString(),
                userData.value.email,
                userData.value.password,
                phoneNumberTextField.text.toString(),
                addressTextField.text.toString()
            )

            val userDataDatabase = UserClass(
                userData.value.id,
                firstNameTextField.text.toString(),
                lastNameTextField.text.toString(),
                userData.value.email,
                userData.value.password,
                phoneNumberTextField.text.toString(),
                addressTextField.text.toString(),
                true
            )

            profileRepository.editeProfile(userData.value.id, userDataFireBase, userDataDatabase)
        }
        return "Success"
    }


    //       *** ---------------------------- \\***  Birthday Sheet  ***// ---------------------------- ***

    val state = MutableStateFlow(true)
    val sheetState = MutableStateFlow(false)


    fun stateTrue(){
        state.value = true
        sheetState.value = true
    }
    fun stateFalse(){
        state.value = false
        sheetState.value = false
    }


    //       *** ---------------------------- \\***  Birthday  ***// ---------------------------- ***

    var selectedDay = MutableStateFlow(1)
    var selectedMonth = MutableStateFlow(1)
    var selectedYear = MutableStateFlow(2000)

    fun birthday(day : Int, month : Int, year : Int){
        selectedDay.value = day
        selectedMonth.value = month
        selectedYear.value = year
    }

    fun setDay(newDay : Int){
        selectedDay.value = newDay
    }

    fun setMonth(newMonth : Int){
        selectedDay.value = newMonth
    }

    fun setYear(newYear : Int){
        selectedDay.value = newYear
    }
}
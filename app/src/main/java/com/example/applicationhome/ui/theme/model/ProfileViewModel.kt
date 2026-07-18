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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val selectedGovernorate = MutableStateFlow("")

    val selectedCity = MutableStateFlow("")

    val searchString = MutableStateFlow("")

    private val allLocations = profileRepository.getLocations()

    val filteredGovernoratesList = searchString
        .map { item ->
            allLocations.filter { it.name.startsWith(item, ignoreCase = true) }
                .map { it.name }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val allCities = selectedGovernorate
        .map { governorate ->
            allLocations.find { it.name == governorate }?.cities ?: emptyList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredCitiesList = combine(
        allCities,
        searchString
    ){ cities, search ->
        cities.filter {
            it.englishName.startsWith(search, ignoreCase = true) ||
            it.arabicName .startsWith(search, ignoreCase = true)
        }
            .map { it.englishName }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun selectDate(date : String){
        selectedDate.value = date
    }

    fun selectGovernorate(governorate : String){
        selectedCity.value = ""
        selectedGovernorate.value = governorate
    }
    fun unselectGovernorate(){
        selectedCity.value = ""
        selectedGovernorate.value = ""
    }

    fun selectCity(city : String){
        selectedCity.value = city
    }
    fun unselectCity(){
        selectedCity.value = ""
    }

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

                selectedGovernorate.value = user.governorate

                selectedCity.value = user.city
            }
        }
    }

    fun filterCities(search : String){
        searchString.value = search
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
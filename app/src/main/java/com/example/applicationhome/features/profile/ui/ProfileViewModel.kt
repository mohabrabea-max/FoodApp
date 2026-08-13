package com.example.applicationhome.features.profile.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.ProfileRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.AccountTextFieldClass
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.entity.UserClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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


    //------------------------------- States ------------------------------
    val loading = profileRepository.loading
    private val _isButtonClicked = MutableStateFlow(false)
    val isButtonClicked = _isButtonClicked.asStateFlow()
    val isDataEdited = profileRepository.isDataEdited



    //------------------------------- Text Fields ------------------------------
    private val firstNameTextField = TextFieldState()
    private val lastNameTextField = TextFieldState()
    private val phoneNumberTextField = TextFieldState()
    private val addressTextField = TextFieldState()

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


    //------------------------------- Text Flows ------------------------------
    private val _selectedDate = MutableStateFlow("")
    val selectedDate = _selectedDate.asStateFlow()

    private val _selectedGovernorate = MutableStateFlow("")
    val selectedGovernorate = _selectedGovernorate.asStateFlow()

    private val _selectedCity = MutableStateFlow("")
    val selectedCity = _selectedCity.asStateFlow()

    private val _searchString = MutableStateFlow("")
    val searchString : StateFlow<String> = _searchString.asStateFlow()


    //------------------------------- Locations ------------------------------
    private val allLocations = profileRepository.getLocations()

    private val allCities = _selectedGovernorate
        .map { governorate ->
            allLocations.find { it.name == governorate }?.cities ?: emptyList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val filteredGovernoratesList = _searchString
        .map { item ->
            allLocations.filter { it.name.contains(item, ignoreCase = true) }
                .map { it.name }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredCitiesList = combine(
        allCities,
        _searchString
    ) { cities, search ->
        cities.filter {
            it.englishName.contains(search, ignoreCase = true) ||
                    it.arabicName.contains(search, ignoreCase = true)
        }
            .map { it.englishName }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    //------------------------------- Init ------------------------------
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

                _selectedDate.value = user.birthday

                _selectedGovernorate.value = user.governorate

                _selectedCity.value = user.city
            }
        }


        profileTextFields.forEach { item ->
            viewModelScope.launch {
                snapshotFlow { item.textField.text.toString() }
                    .collect { isDataChanged() }
            }
        }


        val allTextFlows = listOf(
            _selectedDate,
            _selectedGovernorate,
            _selectedCity
        )

        allTextFlows.forEach { item ->
            viewModelScope.launch {
                item.collect { isDataChanged() }
            }
        }
    }



    //------------------------------- Selection Functions ------------------------------
    fun selectDate(date : String){
        _selectedDate.value = date
        isDataChanged()
    }

    fun selectGovernorate(governorate : String){
        _selectedCity.value = ""
        _selectedGovernorate.value = governorate
        isDataChanged()
    }
    fun unselectGovernorate(){
        _selectedCity.value = ""
        _selectedGovernorate.value = ""
        isDataChanged()
    }

    fun selectCity(city : String){
        _selectedCity.value = city
        isDataChanged()
    }
    fun unselectCity(){
        _selectedCity.value = ""
        isDataChanged()
    }


    //------------------------------- Checked Functions ------------------------------
    fun searchFilter(search : String){
        _searchString.value = search
    }

    private fun buttonClick(){
        _isButtonClicked.value = true
    }

    private fun isDataChanged(){
        val userDataDatabase = UserClass(
            id = userData.value.id,
            firstname = firstNameTextField.text.toString(),
            lastname = lastNameTextField.text.toString(),
            email = userData.value.email,
            phonenumber = phoneNumberTextField.text.toString(),
            birthday = _selectedDate.value,
            governorate = _selectedGovernorate.value,
            city = _selectedCity.value,
            address = addressTextField.text.toString(),
            isActive = true
        )

        profileRepository.changeIsDataEditedState(userData.value != userDataDatabase)
    }


    //       *** --------------------- \\*** Errors And Edite Profile  ***// ---------------------- ***

    private val _editeProfileError = MutableStateFlow<ProfileEditResult?>(null)
    val editeProfileError = _editeProfileError.asStateFlow()

    fun editeProfile() {
        if(
            firstNameTextField.text.isEmpty() ||
            lastNameTextField.text.isEmpty()
        ){
            buttonClick()
            _editeProfileError.value = ProfileEditResult.DataIncomplete
            return
        }

        val validPrefixes = listOf("010", "011", "012", "015")
        if(
            (
                phoneNumberTextField.text.length != 11
                || !validPrefixes.any { phoneNumberTextField.text.contains(it) }
            )
            && phoneNumberTextField.text.isNotEmpty()
        ){
            _editeProfileError.value = ProfileEditResult.PhoneNumberIncomplete
            return
        }

        viewModelScope.launch {
            val userDataFireBase = UserClassFireBase(
                firstname = firstNameTextField.text.toString(),
                lastname = lastNameTextField.text.toString(),
                email = userData.value.email,
                phonenumber = phoneNumberTextField.text.toString(),
                birthday = _selectedDate.value,
                governorate = _selectedGovernorate.value,
                city = _selectedCity.value,
                address = addressTextField.text.toString()
            )

            val userDataDatabase = UserClass(
                id = userData.value.id,
                firstname = firstNameTextField.text.toString(),
                lastname = lastNameTextField.text.toString(),
                email = userData.value.email,
                phonenumber = phoneNumberTextField.text.toString(),
                birthday = _selectedDate.value,
                governorate = _selectedGovernorate.value,
                city = _selectedCity.value,
                address = addressTextField.text.toString(),
                isActive = true
            )

            profileRepository.editeProfile(userData.value.id, userDataFireBase, userDataDatabase)
        }
        _editeProfileError.value = ProfileEditResult.Success
    }
}
package com.example.applicationhome.features.signupscreen.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.Implementations.SupabaseRepositoryImpl
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.ProfileRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.AuthError
import com.example.applicationhome.data.data.model.LoginStates
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.data.data.model.SignUpFullNameTextFields
import com.example.applicationhome.data.data.model.SignUpScreens
import com.example.applicationhome.data.data.model.TextFieldsTypes
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val searchRepository: SearchRepository,
    private val profileRepository : ProfileRepository,
    private val supabaseRepositoryImpl : SupabaseRepositoryImpl,
    networkObserver: NetworkObserver
) : ViewModel() {
    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )


    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    val firstnamestate = TextFieldState()
    val lastnamestate = TextFieldState()

    val emailstate = TextFieldState()

    val passwordstate = TextFieldState()

    val confirmpasswordstate = TextFieldState()

    private val _signUpFullNameTextFields = MutableStateFlow(
        listOf(
            SignUpFullNameTextFields(
                title = "First Name",
                textField = firstnamestate,
                errorMessage = false,
                RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp),
                40.dp,
                0.dp
            ),

            SignUpFullNameTextFields(
                title = "Last Name",
                textField = lastnamestate,
                errorMessage = false,
                RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp),
                0.dp,
                40.dp
            )
        )
    )
    val signUpFullNameTextFields = _signUpFullNameTextFields.asStateFlow()

    private val _signUpBasicTextFields = MutableStateFlow(
        listOf(
            SignUpBasicTextFields(
                title = "Email address",
                textField = emailstate,
                errorMessage = null,
                icon = Icons.Default.Email,
                type = TextFieldsTypes.Basic
            ),

            SignUpBasicTextFields(
                title = "Password",
                textField = passwordstate,
                errorMessage = null,
                icon = Icons.Default.Lock,
                type = TextFieldsTypes.Password
            ),

            SignUpBasicTextFields(
                title = "Confirm password",
                textField = confirmpasswordstate,
                errorMessage = null,
                icon = Icons.Default.Lock,
                type = TextFieldsTypes.Password
            )
        )
    )
    val signUpBasicTextFields = _signUpBasicTextFields.asStateFlow()


    val phonenumberstate = TextFieldState()
    val addressstate = TextFieldState()


    val isButtonEnabled = snapshotFlow {
        firstnamestate.text.isNotEmpty() &&
        lastnamestate.text.isNotEmpty() &&
        emailstate.text.isNotEmpty() &&
        passwordstate.text.isNotEmpty() &&
        confirmpasswordstate.text.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )



    private val _signupPages = MutableStateFlow<SignUpScreens>(SignUpScreens.BasicDataScreen)
    val signupPages = _signupPages.asStateFlow()

    private val id = MutableStateFlow("")


    init {
        observeInputChanges()
    }


    private fun observeInputChanges(){
        viewModelScope.launch {
            snapshotFlow { firstnamestate.text }
                .drop(1)
                .collect {
                    clearErrorForFullNameTextFields(firstnamestate)
                }
        }

        viewModelScope.launch {
            snapshotFlow { lastnamestate.text }
                .drop(1)
                .collect {
                    clearErrorForFullNameTextFields(lastnamestate)
                }
        }

        viewModelScope.launch {
            snapshotFlow { emailstate.text }
                .drop(1)
                .collect {
                    clearErrorForField(emailstate)
                }
        }

        viewModelScope.launch {
            snapshotFlow { passwordstate.text }
                .drop(1)
                .collect {
                    clearErrorForField(passwordstate)
                }
        }

        viewModelScope.launch {
            snapshotFlow { confirmpasswordstate.text }
                .drop(1)
                .collect {
                    clearErrorForField(confirmpasswordstate)
                }
        }
    }

    private fun clearErrorForField(fieldTitle : TextFieldState){
        _signUpBasicTextFields.update {
            it.map { item ->
                if(item.textField == fieldTitle && item.errorMessage != null){
                    item.copy(errorMessage = null)
                }else{
                    item
                }
            }
        }
    }

    private fun clearErrorForFullNameTextFields(fieldTitle : TextFieldState){
        _signUpFullNameTextFields.update {
            it.map { item ->
                if(item.textField == fieldTitle && item.errorMessage){
                    item.copy(errorMessage = false)
                }else{
                    item
                }
            }
        }
    }

    fun signUpButton(onSuccess : () -> Unit, onField : () -> Unit){
        viewModelScope.launch {
            _loading.value = true

            val userData = UserClass(
                id = id.value,
                phonenumber = phonenumberstate.text.toString(),
                address = addressstate.text.toString(),
                isActive = true
            )

            val result = profileRepository.editeProfile(
                id.value,
                UserClassFireBase(
                    phonenumber = phonenumberstate.text.toString(),
                    address = addressstate.text.toString()
                ),
                userData
            )

            when(result){
                LoginStates.Success -> {
                    favoriteRepository.addGuestFavoriteToUser(id.value)
                    searchRepository.addGuestSearchHistoryToUser(id.value)

                    onSuccess()
                }

                is LoginStates.NetworkError -> {
                    onField()
                }
            }

            _loading.value = false
        }
    }

    fun bottonstate(){
        val firstNameStateError = firstnamestate.text.length < 2
        val lastNameStateError = lastnamestate.text.length < 2

        _signUpFullNameTextFields.update { item ->
            item.map { field ->
                when(field.textField){
                    firstnamestate -> {
                        field.copy(errorMessage = firstNameStateError)
                    }

                    lastnamestate -> {
                        field.copy(errorMessage = lastNameStateError)
                    }

                    else -> {
                        field
                    }
                }
            }
        }


        val allowedEmail = "^[A-Za-z0-9._%+-]+@(gmail|yahoo|outlook)\\.(com|net)$".toRegex(RegexOption.IGNORE_CASE)
        val isEmailValid = emailstate.text.matches(allowedEmail)

        val emailErrorMessage = if(!isEmailValid || emailstate.text.isEmpty()){
            "Please enter a valid email address"
        }else{
            null
        }

        val passwordErrorMessage =
            if(
                passwordstate.text.isEmpty() ||
                passwordstate.text.length < 8
            ){
                "Password must be at least 8 characters long"
            }else{
                null
            }

        val confirmPasswordErrorMessage =
            if(
                passwordErrorMessage == null &&
                passwordstate.text != confirmpasswordstate.text
            ){
                "Passwords do not match"
            }else{
                null
            }

        _signUpBasicTextFields.update { item ->
            item.map { field ->
                when(field.textField){
                    emailstate -> {
                        field.copy(errorMessage = emailErrorMessage)
                    }

                    passwordstate -> {
                        field.copy(errorMessage = passwordErrorMessage)
                    }

                    confirmpasswordstate -> {
                        field.copy(errorMessage = confirmPasswordErrorMessage)
                    }

                    else -> field
                }
            }
        }


        if(
            firstnamestate.text.isNotEmpty()
            && lastnamestate.text.isNotEmpty()
            && emailstate.text.isNotEmpty()
            && isEmailValid
            && passwordstate.text.isNotEmpty()
            && passwordstate.text.length >= 8
            && passwordstate.text == confirmpasswordstate.text

        ){
            chickEmail()
            nextPage()
        }
    }

    private fun chickEmail(){
        if(_loading.value) { return }

        viewModelScope.launch {

            bottonstate()

            _loading.value = true

            supabaseRepositoryImpl.signUp(emailstate.text.toString(), passwordstate.text.toString())
                .onSuccess { userId ->
                    id.value = userId

                    userRepository.signUp(
                        userId,
                        UserClassFireBase(
                            firstname = firstnamestate.text.toString(),
                            lastname = lastnamestate.text.toString(),
                            email = emailstate.text.toString()
                        )
                    ).onSuccess { userId ->
                        favoriteRepository.addGuestFavoriteToUser(userId)
                        searchRepository.addGuestSearchHistoryToUser(userId)
                    }.onFailure { error ->

                    }
                }.onFailure { error ->
                    val authError = (error as? SupabaseRepositoryImpl.AuthException)?.error
                        ?: AuthError.UnknownError(error.message ?: "")

                    when (authError) {
                        is AuthError.NetworkError -> {
                            //showNetworkSnackbar()
                        }

                        is AuthError.EmailAlreadyExists -> {
                            _signUpBasicTextFields.update { item ->
                                item.map {
                                    if (it.textField == emailstate){
                                        it.copy(errorMessage = "Please enter a valid email address")
                                    }else{
                                        it
                                    }
                                }

                            }
                        }

                        is AuthError.UnknownError -> {
                            //showGeneralError(authError.message)
                        }
                    }
                }

            _loading.value = false
        }
    }

    private fun nextPage(){
        _signupPages.value = SignUpScreens.OptionalDataScreen
    }

    fun lastPage(popBackStack : () -> Unit){
        when(_signupPages.value){
            SignUpScreens.OptionalDataScreen -> {
                _signupPages.value = SignUpScreens.BasicDataScreen
            }

            SignUpScreens.BasicDataScreen -> {
                popBackStack()
            }
        }
    }
}
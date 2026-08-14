package com.example.applicationhome.features.login.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.applicationhome.core.domain.exception.AppDomainException
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.ErrorsType
import com.example.applicationhome.data.data.model.LoginStates
import com.example.applicationhome.data.data.model.LoginTextFields
import com.example.applicationhome.data.data.model.SignUpErrors
import com.example.applicationhome.data.data.model.TextFieldsTypes
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val searchRepository: SearchRepository,
    private val supabaseRepository: SupabaseRepository,
    networkObserver: NetworkObserver
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    val emailTextField = savedStateHandle.saveable(
        key = "emailTextField",
        saver = TextFieldState.Saver
    ){
        TextFieldState()
    }

    val passwordTextField = savedStateHandle.saveable(
        key = "passwordTextField",
        saver = TextFieldState.Saver
    ){
        TextFieldState()
    }

    private val _loginTextFields = MutableStateFlow(
        listOf(
            LoginTextFields(
                title = "Email address",
                textField = emailTextField,
                icon = Icons.Default.Email,
                type = TextFieldsTypes.Basic
            ),

            LoginTextFields(
                title = "Password",
                textField = passwordTextField,
                icon = Icons.Default.Lock,
                type = TextFieldsTypes.Password
            )
        )
    )
    val loginTextFields = _loginTextFields.asStateFlow()

    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val userData = userRepository.userData

    val isButtonLoginPageEnabled = snapshotFlow {
        emailTextField.text.isNotEmpty() &&
        passwordTextField.text.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private val _signUpStates = Channel<SignUpErrors>(Channel.BUFFERED)
    val signUpStates = _signUpStates.receiveAsFlow()

    fun snackbarError(message : String){
        viewModelScope.launch {
            _signUpStates.send(SignUpErrors.Error(message))
        }
    }



    fun logout(){
        viewModelScope.launch {
            userRepository.logOut(userData.value.email)
            userRepository.logout()
        }
    }

    private val _errorType = MutableStateFlow<LoginStates>(LoginStates.Success)
    val errorType = _errorType.asStateFlow()

    fun login(onSuccess : () -> Unit, onField : () -> Unit){

        viewModelScope.launch {
            _loading.value = true

            supabaseRepository.login(emailTextField.text.toString(), passwordTextField.text.toString())
                .onSuccess { userId ->
                    val firebaseResult = userRepository.setUserDataToDatabase(emailTextField.text.toString())

                    when (firebaseResult) {
                        is LoginStates.Success -> {
                            userRepository.login()

                            onSuccess()

                            favoriteRepository.addGuestFavoriteToUser(userId)

                            searchRepository.addGuestSearchHistoryToUser(userId)
                        }

                        is LoginStates.Error -> {
                            onField()
                        }
                    }
                }
                .onFailure { error ->
                    val domainError = (error as? AppDomainException)?.errorType

                    when(domainError){
                        ErrorsType.DATA -> {
                            _errorType.value = LoginStates.Error("Incorrect email or password")
                        }

                        ErrorsType.NETWORK -> {
                            snackbarError("Network error")
                        }

                        else -> { snackbarError("Unknown error") }
                    }

                    onField()
                }

            _loading.value = false
        }
    }


    init {
        viewModelScope.launch {
            userData.collect { currentUser ->
                if(currentUser.id.isNotEmpty()){
                    userRepository.login()
                }else{
                    userRepository.logout()
                }
            }
        }
    }
}
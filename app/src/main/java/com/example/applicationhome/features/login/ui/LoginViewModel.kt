package com.example.applicationhome.features.login.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.ChickEmailStates
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
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
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val searchRepository: SearchRepository,
    networkObserver: NetworkObserver
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    val emailTextField = TextFieldState()
    val passwordTextField = TextFieldState()

    private val _loginTextFields = MutableStateFlow(
        listOf(
            SignUpBasicTextFields(
                title = "Email address",
                textField = emailTextField,
                errorMessage = null,
                icon = Icons.Default.Email
            ),

            SignUpBasicTextFields(
                title = "Password",
                textField = passwordTextField,
                errorMessage = null,
                icon = Icons.Default.Lock
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


    private fun observeInputChanges(){
        viewModelScope.launch {
            snapshotFlow { emailTextField.text }
                .drop(1)
                .collect {
                    clearErrorForField(emailTextField)
                }
        }

        viewModelScope.launch {
            snapshotFlow { passwordTextField.text }
                .drop(1)
                .collect {
                    clearErrorForField(passwordTextField)
                }
        }
    }

    private fun clearErrorForField(fieldTitle : TextFieldState) {
        _loginTextFields.update {
            it.map { item ->
                if(item.textField == fieldTitle && item.errorMessage != null){
                    item.copy(errorMessage = null)
                }else{
                    item
                }
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            userRepository.logOut(userData.value.email)
            userRepository.logout()
        }
    }

    fun login(onSuccess : () -> Unit, onField : () -> Unit){
        if (_loading.value) { return }

        viewModelScope.launch {
            _loading.value = true

            val result = userRepository.setUserDataToDatabase(emailTextField.text.toString(), passwordTextField.text.toString())

            when (result) {
                is ChickEmailStates.PasswordTrue -> {
                    userRepository.login()

                    onSuccess()

                    favoriteRepository.addGuestFavoriteToUser(result.userId)

                    searchRepository.addGuestSearchHistoryToUser(result.userId)
                }

                is ChickEmailStates.EmailFalse -> {

                    onField()

                    _loginTextFields.update { item ->
                        item.map { field ->
                            when(field.textField){
                                emailTextField -> {
                                    field.copy(errorMessage = "Please enter a valid email address")
                                }

                                passwordTextField -> {
                                    field.copy(errorMessage = null)
                                }

                                else -> field
                            }
                        }
                    }
                }

                is ChickEmailStates.PasswordFalse -> {

                    onField()

                    _loginTextFields.update { item ->
                        item.map { field ->
                            when(field.textField){
                                passwordTextField -> {
                                    field.copy(errorMessage = "Please enter a valid password")
                                }

                                emailTextField -> {
                                    field.copy(errorMessage = null)
                                }

                                else -> field
                            }
                        }
                    }
                }

                else -> { onField() }
            }

            _loading.value = false
        }
    }


    init {
        observeInputChanges()

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
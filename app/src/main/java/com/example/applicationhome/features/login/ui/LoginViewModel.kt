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

    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()

    private val _loginTextFields = MutableStateFlow(
        listOf(
            SignUpBasicTextFields(
                title = "Email address",
                textField = emailstate,
                errorMessage = null,
                icon = Icons.Default.Email
            ),

            SignUpBasicTextFields(
                title = "Password",
                textField = passwordstate,
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

    val isButtonEnabled = snapshotFlow {
        emailstate.text.isNotEmpty() &&
        passwordstate.text.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


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


    fun logout(){
        viewModelScope.launch {
            userRepository.logOut(userData.value.email)
            userRepository.logout()
        }
    }

    fun login(onSuccess : () -> Unit, onField : () -> Unit){
        viewModelScope.launch {
            _loading.value = true

            val result = userRepository.setUserDataToDatabase(emailstate.text.toString(), passwordstate.text.toString())

            when (result) {
                is ChickEmailStates.PasswordTrue -> {
                    userRepository.login()

                    favoriteRepository.addGuestFavoriteToUser(result.userId)

                    searchRepository.addGuestSearchHistoryToUser(result.userId)

                    onSuccess()
                }

                is ChickEmailStates.EmailFalse -> {

                    onField()

                    _loginTextFields.update { item ->
                        item.map { field ->
                            when(field.textField){
                                emailstate -> {
                                    field.copy(errorMessage = "Invalid email.")
                                }

                                passwordstate -> {
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
                                passwordstate -> {
                                    field.copy(errorMessage = "Invalid password.")
                                }

                                emailstate -> {
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
}
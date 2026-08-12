package com.example.applicationhome.features.login.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.Implementations.SupabaseRepositoryImpl
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SearchRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.ui.theme.BrandBlue
import com.example.applicationhome.data.data.model.ChickEmailStates
import com.example.applicationhome.data.data.model.LoginPages
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.data.data.model.VerificationTextFields
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val searchRepository: SearchRepository,
    private val supabaseRepositoryImpl : SupabaseRepositoryImpl,
    networkObserver: NetworkObserver
) : ViewModel() {

    // --------------------------------------------\\ Login Page //--------------------------------------------

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

    val isButtonCheckEmailPageEnabled = snapshotFlow {
        checkEmailTextField.text.isNotEmpty()
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



        viewModelScope.launch {
            snapshotFlow { checkEmailTextField.text }
                .drop(1)
                .collect {
                    clearErrorForField(checkEmailTextField)
                }
        }


        viewModelScope.launch {
            snapshotFlow { verificationCodeTextField.text }
                .drop(1)
                .collect {
                    clearErrorForField(verificationCodeTextField)
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

        _checkEmailTextFieldObject.update {
            if(it.textField == fieldTitle && it.errorMessage != null){
                it.copy(errorMessage = null)
            }else{
                it
            }
        }

        _verificationCodeTextFieldObject.update {
            if(it.textField == fieldTitle && it.error){
                it.copy(error = false, stateColor = Color.BrandBlue)
            }else{
                it
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


    // --------------------------------------------\\ Chick Email Page //--------------------------------------------
    val checkEmailTextField = TextFieldState()
    private val _checkEmailTextFieldObject = MutableStateFlow(
        SignUpBasicTextFields(
            title = "Email address",
            textField = checkEmailTextField,
            errorMessage = null,
            icon = Icons.Default.Email
        )
    )
    val checkEmailTextFieldObject = _checkEmailTextFieldObject.asStateFlow()


    fun chickEmail(onChangeClickState : (Boolean) -> Unit){
        if (_loading.value) { return }

        viewModelScope.launch {
            _loading.value = true

            val result = userRepository.checkEmailInApi(checkEmailTextField.text.toString())

            when(result){
                ChickEmailStates.EmailTrue -> {
                    navigateTo(LoginPages.VerificationCodePage){
                        onChangeClickState(true)
                        sendVerificationCode()
                    }
                }

                else -> {
                    _checkEmailTextFieldObject.update {
                        it.copy(errorMessage = "Please enter a valid email address")
                    }

                    onChangeClickState(true)
                }
            }

            _loading.value = false
        }
    }


    // --------------------------------------------\\ Verification Code Page //--------------------------------------------
    val verificationCodeTextField = TextFieldState()
    private val _verificationCodeTextFieldObject = MutableStateFlow(
        VerificationTextFields(
            title = "",
            textField = verificationCodeTextField,
            error = false,
            stateColor = Color.BrandBlue
        )
    )
    val verificationCodeTextFieldObject = _verificationCodeTextFieldObject.asStateFlow()

    private val _verificationCodeLoading = MutableStateFlow(false)
    val verificationCodeLoading = _verificationCodeLoading.asStateFlow()


    fun sendVerificationCode(){
        if(_verificationCodeLoading.value) { return }

        viewModelScope.launch {
            _verificationCodeLoading.value = true

            supabaseRepositoryImpl.sendOtp(checkEmailTextField.text.toString())
                .onSuccess {

                }
                .onFailure { error ->

                }

            _verificationCodeLoading.value = false
        }
    }

    fun checkVerificationCode(){
        if (_loading.value) { return }

        viewModelScope.launch {
            _loading.value = true

            supabaseRepositoryImpl.verifyOtp(checkEmailTextField.text.toString(), verificationCodeTextField.text.toString())
                .onSuccess {
                    _verificationCodeTextFieldObject.update {
                        it.copy(error = false, stateColor = Color.Green)
                    }

                    navigateTo(LoginPages.ChangePasswordPage){}
                }
                .onFailure {
                    _verificationCodeTextFieldObject.update {
                        it.copy(error = true, stateColor = Color.Red)
                    }
                }

            _loading.value = false
        }
    }


    // --------------------------------------------\\ Change Password Page //--------------------------------------------
    val newPasswordTextField = TextFieldState()
    val confirmNewPasswordTextField = TextFieldState()


    // --------------------------------------------\\ Pages //--------------------------------------------
    val _backStack = MutableStateFlow<List<LoginPages>>(listOf(LoginPages.LoginPage))

    val currentScreen =
        _backStack.map { it.last() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = LoginPages.LoginPage
            )

    fun navigateTo(screen : LoginPages, onChangeClickState : (Boolean) -> Unit){
        when(screen){
            LoginPages.ChangePasswordPage -> {
                _backStack.update { listOf(LoginPages.LoginPage) }

                clearAllTextFields()
            }

            else -> {
                _backStack.update { it + screen }
            }
        }

        onChangeClickState(true)
    }

    fun navigateBack(onExitLoginScreen : () -> Unit, onChangeClickState : (Boolean) -> Unit){
        _backStack.update { currentStack ->
            if(currentStack.size > 1){
                currentStack.dropLast(1)
            }else{
                onExitLoginScreen()
                currentStack
            }
        }

        onChangeClickState(true)
    }

    private fun clearAllTextFields(){
        checkEmailTextField.clearText()
        verificationCodeTextField.clearText()
        newPasswordTextField.clearText()
        confirmNewPasswordTextField.clearText()
    }



    init {
        observeInputChanges()

        viewModelScope.launch {
            snapshotFlow { verificationCodeTextField.text }
                .collect {
                    if (it.length == 6){
                        checkVerificationCode()
                    }
                }
        }

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
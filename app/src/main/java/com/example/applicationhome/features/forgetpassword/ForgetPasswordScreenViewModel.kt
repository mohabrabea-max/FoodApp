package com.example.applicationhome.features.forgetpassword

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.applicationhome.core.domain.exception.AppDomainException
import com.example.applicationhome.core.domain.repository.SupabaseRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.ui.theme.BrandBlue
import com.example.applicationhome.data.data.model.ChickEmailStates
import com.example.applicationhome.data.data.model.ErrorsType
import com.example.applicationhome.data.data.model.LoginPages
import com.example.applicationhome.data.data.model.SignUpBasicTextFields
import com.example.applicationhome.data.data.model.SignUpErrors
import com.example.applicationhome.data.data.model.TextFieldsTypes
import com.example.applicationhome.data.data.model.VerificationTextFields
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ForgetPasswordScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val supabaseRepository: SupabaseRepository,
    networkObserver: NetworkObserver
): ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val userData = userRepository.userData

    private fun observeInputChanges(){
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

        viewModelScope.launch {
            snapshotFlow { newPasswordTextField.text }
                .drop(1)
                .collect {
                    clearErrorForField(newPasswordTextField)
                }
        }
        viewModelScope.launch {
            snapshotFlow { confirmNewPasswordTextField.text }
                .drop(1)
                .collect {
                    clearErrorForField(confirmNewPasswordTextField)
                }
        }
    }

    private fun clearErrorForField(fieldTitle : TextFieldState) {
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

        _newPasswordTextFields.update {
            it.map { item ->
                if(item.textField == fieldTitle && item.errorMessage != null){
                    item.copy(errorMessage = null)
                }else{
                    item
                }
            }
        }
    }


    // --------------------------------------------\\ Chick Email Page //--------------------------------------------
    val isButtonCheckEmailPageEnabled = snapshotFlow {
        checkEmailTextField.text.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val checkEmailTextField = savedStateHandle.saveable(
        key = "checkEmailTextField",
        saver = TextFieldState.Saver
    ){
        TextFieldState()
    }
    private val _checkEmailTextFieldObject = MutableStateFlow(
        SignUpBasicTextFields(
            title = "Email address",
            textField = checkEmailTextField,
            errorMessage = null,
            icon = Icons.Default.Email,
            type = TextFieldsTypes.Basic
        )
    )
    val checkEmailTextFieldObject = _checkEmailTextFieldObject.asStateFlow()


    fun checkEmail(onChangeClickState : () -> Unit){
        if (_loading.value) { return }

        viewModelScope.launch {
            _loading.value = true

            val result = userRepository.checkEmailInApi(checkEmailTextField.text.toString())

            when(result){
                ChickEmailStates.Success -> {
                    navigateTo(LoginPages.VerificationCodePage){}
                }

                ChickEmailStates.EmailIsNotTrue -> {
                    _checkEmailTextFieldObject.update {
                        it.copy(errorMessage = "Please enter a valid email address")
                    }
                }

                is ChickEmailStates.NetworkError -> {
                    snackbarError(SignUpErrors.Error("Network error"))
                }
            }

            onChangeClickState()

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

            supabaseRepository.sendOtp(checkEmailTextField.text.toString())
                .onFailure {
                    snackbarError(SignUpErrors.Error("Failed to send code. Please try again."))
                }

            _verificationCodeLoading.value = false
        }
    }

    private fun checkVerificationCode(){
        if (_loading.value) { return }

        viewModelScope.launch {
            _loading.value = true

            supabaseRepository.verifyOtp(checkEmailTextField.text.toString(), verificationCodeTextField.text.toString())
                .onSuccess {
                    _verificationCodeTextFieldObject.update {
                        it.copy(error = false, stateColor = Color.Green)
                    }

                    navigateTo(LoginPages.ChangePasswordPage){}

                    verificationCodeTextField.clearText()
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
    val isButtonChangePasswordPageEnabled = combine(
        snapshotFlow { newPasswordTextField.text },
        snapshotFlow { confirmNewPasswordTextField.text }
    ){ pass, confirmPass ->
        pass.isNotEmpty() && confirmPass.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val newPasswordTextField = savedStateHandle.saveable(
        key = "newPasswordTextField",
        saver = TextFieldState.Saver
    ){
        TextFieldState()
    }

    val confirmNewPasswordTextField = savedStateHandle.saveable(
        key = "confirmNewPasswordTextField",
        saver = TextFieldState.Saver
    ){
        TextFieldState()
    }

    private val _newPasswordTextFields = MutableStateFlow(
        listOf(
            SignUpBasicTextFields(
                title = "New password",
                textField = newPasswordTextField,
                errorMessage = null,
                icon = Icons.Default.Lock,
                type = TextFieldsTypes.Password
            ),

            SignUpBasicTextFields(
                title = "Confirm password",
                textField = confirmNewPasswordTextField,
                errorMessage = null,
                icon = Icons.Default.Lock,
                type = TextFieldsTypes.Password
            ),
        )
    )
    val newPasswordTextFields = _newPasswordTextFields.asStateFlow()

    fun onChangePasswordClicked(onSuccess: () -> Unit){
        val passwordErrorMessage =
            if(newPasswordTextField.text.length < 8){
                "Password must be at least 8 characters long"
            }else{
                null
            }

        val confirmPasswordErrorMessage =
            if(
                passwordErrorMessage == null &&
                newPasswordTextField.text != confirmNewPasswordTextField.text
            ){
                "Passwords do not match"
            }else{
                null
            }


        _newPasswordTextFields.update {
            it.map { item ->
                when(item.textField){
                    newPasswordTextField -> {
                        item.copy(errorMessage = passwordErrorMessage)
                    }

                    confirmNewPasswordTextField -> {
                        item.copy(errorMessage = confirmPasswordErrorMessage)
                    }

                    else -> item
                }
            }
        }


        if(
            passwordErrorMessage == null
            && confirmPasswordErrorMessage == null
        ){
            changePassword{
                onSuccess()
            }
        }
    }

    private fun changePassword(onSuccess : () -> Unit){
        if (_loading.value) { return }

        viewModelScope.launch {
            _loading.value = true

            supabaseRepository.updatePassword(newPasswordTextField.text.toString())
                .onSuccess {
                    onSuccess()
                }
                .onFailure { error ->
                    val authError = (error as? AppDomainException)?.errorType

                    when(authError){
                        ErrorsType.DATA -> {
                            _newPasswordTextFields.update {
                                it.map { item ->
                                    when(item.textField){
                                        newPasswordTextField -> {
                                            item.copy(errorMessage = "Incorrect password")
                                        }

                                        else -> item
                                    }
                                }
                            }
                        }

                        ErrorsType.NETWORK -> {
                            snackbarError(SignUpErrors.Error("Network error"))
                        }

                        else -> { snackbarError(SignUpErrors.Error("Unknown error")) }
                    }
                }

            _loading.value = false
        }
    }


    // --------------------------------------------\\ Pages //--------------------------------------------
    val _backStack = MutableStateFlow<List<LoginPages>>(listOf(LoginPages.EmailPage))

    val currentScreen =
        _backStack.map { it.last() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = LoginPages.EmailPage
            )

    fun navigateTo(screen : LoginPages, navigation : () -> Unit){
        when(_backStack.map { it.last() }){
            LoginPages.ChangePasswordPage -> {
                navigation()
            }

            else -> {
                if(screen == LoginPages.VerificationCodePage){
                    sendVerificationCode()

                    _backStack.update { it + screen }
                }else{
                    _backStack.update { it + screen }
                }
            }
        }
    }

    fun navigateBack(onExitLoginScreen : () -> Unit, onChangeClickState : (Boolean) -> Unit){
        _backStack.update { currentStack ->
            when(currentStack.last()){
                LoginPages.ChangePasswordPage -> {
                    currentStack.dropLast(2)
                }

                else -> {
                    if(currentStack.size > 1){
                        currentStack.dropLast(1)
                    }else{
                        onExitLoginScreen()
                        currentStack
                    }
                }
            }
        }

        onChangeClickState(true)
    }


    private val _signUpStates = Channel<SignUpErrors>(Channel.BUFFERED)
    val signUpStates = _signUpStates.receiveAsFlow()


    fun snackbarError(message : SignUpErrors){
        viewModelScope.launch {
            _signUpStates.send(message)
        }
    }


    init {
        observeInputChanges()

        viewModelScope.launch {
            snapshotFlow { verificationCodeTextField.text }
                .collect {
                    if (it.length == 6) {
                        checkVerificationCode()
                    }
                }
        }
    }
}
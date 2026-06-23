package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.UserClassFireBase
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository, application : Application) : AndroidViewModel(application) {
    val firstnamestate = TextFieldState()
    val lastnamestate = TextFieldState()
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()
    val confirmpasswordstate = TextFieldState()
    val phonenumberstate = TextFieldState()
    val addressstate = TextFieldState()

    var bottonState by mutableStateOf(false)


    private val _signUpResult = MutableLiveData<String>()

    var signupPages by mutableStateOf(1)

    fun signUpButton(){
        viewModelScope.launch {
            userRepository.signUp(UserClassFireBase(
                firstnamestate.text.toString(),
                lastnamestate.text.toString(),
                emailstate.text.toString(),
                passwordstate.text.toString(),
                phonenumberstate.text.toString(),
                addressstate.text.toString()
            )
            )
        }
    }

    fun bottonstate(){
        val allowedEmail = "^[A-Za-z0-9._%+-]+@(gmail|yahoo|outlook)\\.(com|net)$".toRegex(RegexOption.IGNORE_CASE)
        val isEmailValid = emailstate.text.matches(allowedEmail)

        if(
            firstnamestate.text.isNotEmpty()
            && lastnamestate.text.isNotEmpty()
            && emailstate.text.isNotEmpty()
            && isEmailValid
            && passwordstate.text.isNotEmpty()
            && passwordstate.text.length >= 8
            && passwordstate.text == confirmpasswordstate.text
        ){
            bottonState = true
        } else {
            bottonState = false
        }
    }

    fun nextPage(){
        viewModelScope.launch {
            val result = userRepository.setUserDataToDatabase(emailstate.text.toString(), null)
            if (result == "Email is false") {
                signupPages += 1
            }
        }
    }

    fun lastPage(){
        signupPages -= 1
    }
}
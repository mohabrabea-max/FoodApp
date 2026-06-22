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
import com.example.applicationhome.data.models.repository.UserRepository.signUp
import com.example.applicationhome.data.models.repository.UserRepository.userData
import com.example.applicationhome.data.models.repository.UserRepository.userId
import kotlinx.coroutines.launch

class SignUpViewModel(application : Application) : AndroidViewModel(application) {
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
            userData = userData.copy(
                id = userId,
                firstname = firstnamestate.text.toString(),
                lastname = lastnamestate.text.toString(),
                email = emailstate.text.toString(),
                password = passwordstate.text.toString(),
                phonenumber = phonenumberstate.text.toString(),
                address = addressstate.text.toString(),
                isActive = true
            )
            signUp(UserClassFireBase(
                userData.firstname,
                userData.lastname,
                userData.email,
                userData.password,
                userData.phonenumber,
                userData.address
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
            val result = UserRepository.setUserDataToDatabase(emailstate.text.toString(), null)
            if (result == "Email is false") {
                signupPages += 1
            }
        }
    }

    fun lastPage(){
        signupPages -= 1
    }
}
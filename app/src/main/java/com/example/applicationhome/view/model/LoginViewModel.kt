package com.example.applicationhome.view.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.RetrofitInstance
import com.example.applicationhome.data.models.UserClass
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var isLogin by mutableStateOf(false)
    var userData by mutableStateOf(
        UserClass(
            "Guest",
            null,
            null,
            null,
            null,
            null
        )
    ); private set

    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()

    var isEmailTrue by mutableStateOf(false)
    var isPasswordTrue by mutableStateOf(false)



    fun getDataInScreen(){
        getData(emailstate.text.toString(), passwordstate.text.toString())
    }

    fun bottonstate(){
        emailstate.clearText()
        passwordstate.clearText()
    }

    fun getData(emailstate : String, passwordstate : String){
        viewModelScope.launch {
            try {
                val formatEmail = "\"$emailstate\""
                val response = RetrofitInstance.api.getUserEmail(email = formatEmail)
                if(response.isSuccessful && response.body() != null){
                    val userMap = response.body()!!
                    if(userMap.isNotEmpty()){
                        isEmailTrue = true
                        if(passwordstate == userMap.values.first().password){
                            isPasswordTrue = true
                            userData = userMap.values.first()
                        }else{
                            isPasswordTrue = false
                        }
                    }else{
                        isEmailTrue = false
                    }
                }
            } catch (e : Exception){
                println("خطأ في الشبكة: ${e.message}")
            }
        }
    }

    fun logout(){
        isLogin = false
        userData = UserClass(
            "Guest",
            null,
            null,
            null,
            null,
            null
        )
    }

    fun login(userdata : UserClass){
        userData = userdata
        isLogin = true
    }
}
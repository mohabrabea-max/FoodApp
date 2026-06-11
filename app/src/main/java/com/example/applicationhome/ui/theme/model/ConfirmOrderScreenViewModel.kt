package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfirmOrderScreenViewModel : ViewModel() {
    val houseState = TextFieldState()
    val streetState = TextFieldState()
    val phoneNumberState = TextFieldState()
    val additionalDirectionsState = TextFieldState()
    val addressLabelState = TextFieldState()

    var bottonState by mutableStateOf(false)

    fun bottonstate(){
        if(
            houseState.text.isNotEmpty()
            && streetState.text.isNotEmpty()
            && phoneNumberState.text.isNotEmpty()
            && phoneNumberState.text.length >= 11
        ){
            bottonState = true
        } else {
            bottonState = false
        }
    }
}
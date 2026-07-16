package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.data.model.Account
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    var profile = userRepository.profileData()

    var newStat by mutableStateOf("")

    fun changeProfileData(account: Account, newData : Any){
        val dataID = profile.indexOfFirst { it.id == account.id }
        newStat = newData.toString()


        if(dataID != -1){
            val textToSave = when (newData) {
                is TextFieldState -> newData.text.toString()
                else -> newData.toString()
            }
        }
    }


    //       *** ---------------------------- \\***  Birthday Sheet  ***// ---------------------------- ***

    var state by mutableStateOf(true)
    var sheetState by mutableStateOf(false)


    fun stateTrue(){
        state = true
        sheetState = true
    }
    fun stateFalse(){
        state = false
        sheetState = false
    }


    //       *** ---------------------------- \\***  Birthday  ***// ---------------------------- ***

    var selectedDay by mutableStateOf(1)
    var selectedMonth by mutableStateOf(1)
    var selectedYear by mutableStateOf(2000)

    fun birthday(day : Int, month : Int, year : Int){
        selectedDay = day
        selectedMonth = month
        selectedYear = year
    }

}
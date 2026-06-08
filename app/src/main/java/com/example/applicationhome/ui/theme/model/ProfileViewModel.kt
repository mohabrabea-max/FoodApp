package com.example.applicationhome.ui.theme.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.models.repository.ProfileData
import com.example.applicationhome.data.models.model.Account

class ProfileViewModel : ViewModel(){
    var profile = mutableStateListOf<Account>().apply {
        addAll(ProfileData.profileData())
    }
    var newStat by mutableStateOf("")

    fun changeProfileData(account: Account, newData : Any){
        val dataID = profile.indexOfFirst { it.id == account.id }
        newStat = newData.toString()


        if(dataID != -1){
            val textToSave = when (newData) {
                is TextFieldState -> newData.text.toString()
                else -> newData.toString()
            }
            profile[dataID] = profile[dataID].copy(value = textToSave)
        }
    }

    fun change(){

    }

}
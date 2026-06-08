package com.example.applicationhome.ui.theme.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserImageViewModel : ViewModel(){
    var selectedImageUri = mutableStateOf<Uri?>(null)

    var stat by mutableStateOf(false)

    fun addPhoto(uri : Uri?){
        selectedImageUri.value = uri
    }

    fun statetrue(){
        stat = true
    }

    fun statefalse(){
        stat = false
    }
}
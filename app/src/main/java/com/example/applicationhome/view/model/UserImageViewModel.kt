package com.example.applicationhome.view.model

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserImageViewModel : ViewModel(){
    var selectedImageUri = mutableStateOf<Uri?>(null)

    fun addPhoto(uri : Uri?){
        selectedImageUri.value = uri
    }
}
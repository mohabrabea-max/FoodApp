package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.domain.model.Governorate
import com.example.applicationhome.core.domain.model.LoginStates
import com.example.applicationhome.core.domain.model.UserClassFireBase
import com.example.applicationhome.core.data.local.entity.UserClass
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    val isDataEdited: StateFlow<Boolean>

    suspend fun editeProfile(
        userId : String,
        userDataFireBase : UserClassFireBase,
        userDataDatabase : UserClass
    ): LoginStates
    fun changeIsDataEditedState(state : Boolean)
    fun getLocations(): List<Governorate>
}
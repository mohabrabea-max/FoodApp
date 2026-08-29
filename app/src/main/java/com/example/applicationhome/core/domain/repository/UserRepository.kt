package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.ChickEmailStates
import com.example.applicationhome.data.data.model.LoginStates
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.entity.UserClass
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val isLogin : StateFlow<Boolean>
    val userData : StateFlow<UserClass>

    suspend fun setUserDataToDatabase(emailstate : String): LoginStates
    suspend fun checkEmailInApi(emailstate : String): ChickEmailStates
    suspend fun logOut()
    suspend fun signUp(userId : String, userRequest : UserClassFireBase): Result<Unit>
    fun login()

    suspend fun updatePhoneNumber(userId : String, newNumber : String)

    suspend fun validateUserOnAppLaunch(): Boolean
    suspend fun observeSessionStatus()
}
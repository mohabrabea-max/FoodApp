package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.Governorate
import com.example.applicationhome.data.data.model.LoginStates
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.dao.UsersDao
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.local.source.LocalLocationDataSource
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api : FoodAppAPIs,
    private val usersDao : UsersDao,
    private val localDataSource : LocalLocationDataSource
) {
    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    private val _isDataEdited = MutableStateFlow(false)
    val isDataEdited = _isDataEdited.asStateFlow()

    suspend fun editeProfile(
        userId : String,
        userDataFireBase : UserClassFireBase,
        userDataDatabase : UserClass
    ): LoginStates {
        return try {
            _loading.value = true
            val response = api.editeProfile(userId, userDataFireBase)
            if(response.isSuccessful){
                usersDao.addUser(userDataDatabase)
                _isDataEdited.value = false
                LoginStates.Success
            }else{
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }

                LoginStates.NetworkError(errorMessage)
            }
        } catch (e: Exception){
            _loading.value = false
            _isDataEdited.value = true
            LoginStates.NetworkError(e.message.toString())
        }
    }

    fun changeIsDataEditedState(state : Boolean){
        _isDataEdited.value = state
    }


    fun getLocations(): List<Governorate> {
        return localDataSource.getLocationsFromJson()
    }
}
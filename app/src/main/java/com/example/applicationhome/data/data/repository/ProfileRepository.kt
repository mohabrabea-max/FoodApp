package com.example.applicationhome.data.data.repository

import com.example.applicationhome.data.data.local.dao.UsersDao
import com.example.applicationhome.data.data.local.entity.UserClass
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.data.remote.FoodAppAPIs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api : FoodAppAPIs,
    private val usersDao: UsersDao
) {
    suspend fun editeProfile(
        userId : String,
        userDataFireBase : UserClassFireBase,
        userDataDatabase : UserClass
    ){
        try {
            val response = api.editeProfile(userId, userDataFireBase)
            if(response.isSuccessful){
                usersDao.addUser(userDataDatabase)
            }
        }catch (e: Exception){  }
    }
}
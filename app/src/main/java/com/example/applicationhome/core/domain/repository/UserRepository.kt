package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.domain.Implementations.SupabaseUserRemoteDataSource
import com.example.applicationhome.data.data.model.ChickEmailStates
import com.example.applicationhome.data.data.model.LoginStates
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.dao.FavoriteDao
import com.example.applicationhome.data.local.dao.UsersDao
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.remote.FoodAppAPIs
import com.example.applicationhome.domain.ApplicationScope
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

//    suspend fun addToMeals(){
//        try {
//            snacks.forEach { (key, value) ->
//                RetrofitInstance.api.addToMeals(key, mapOf("restaurantId" to 0))
//            }
//        }catch (e : Exception){
//            println("")
//        }
//    }

@Singleton
class UserRepository @Inject constructor(
    private val supabaseUserRemoteDataSource : SupabaseUserRemoteDataSource,
    private val userdao : UsersDao,
    private val favoriteDao: FavoriteDao,
    private val api : FoodAppAPIs,
    @ApplicationScope externalScope : CoroutineScope
) {
    private val _isLogin = MutableStateFlow(false)
    val isLogin : StateFlow<Boolean> = _isLogin

    val userData : StateFlow<UserClass> =
        userdao.getActiveUser()
            .map { userInDb ->
                userInDb ?: UserClass(firstname = "Guest")
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserClass(firstname = "Guest")
            )


    suspend fun setUserDataToDatabase(emailstate : String): LoginStates {
        return try {
            val formatEmail = "\"$emailstate\""
            val response = api.getUserData(order = "\"email\"", value = formatEmail)
            val userMap = response.body()
            val user = userMap?.values?.firstOrNull()

            if(response.isSuccessful && user != null){

                val userId = userMap.keys.firstOrNull().toString()
                val data = UserClass(
                    userId,
                    user.firstname,
                    user.lastname,
                    user.email,
                    user.phonenumber,
                    user.birthday,
                    user.governorate,
                    user.city,
                    user.address
                )
                userdao.addUser(data)
                LoginStates.Success

            } else {
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                LoginStates.Error(errorMessage)
            }
        } catch (e : Exception){
            LoginStates.Error("خطأ في الشبكة: ${e.message}")
        }
    }

    suspend fun checkEmailInApi(emailstate : String): ChickEmailStates{
        return try {
            val formatEmail = "\"$emailstate\""
            val response = api.getUserData(order = "\"email\"", value = formatEmail)
            val userMap = response.body()
            if(response.isSuccessful){
                if(userMap.isNullOrEmpty()){
                    ChickEmailStates.EmailIsNotTrue
                }else{
                    ChickEmailStates.Success
                }
            }else{
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                ChickEmailStates.NetworkError(errorMessage)
            }
        } catch (e : Exception){
            ChickEmailStates.NetworkError("خطأ في الشبكة: ${e.message}")
        }
    }


    suspend fun logOut(): String{
        return try {
            userdao.deleteUserFromDatabase()
            "Success"
        } catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun signUp(userId : String, userRequest : UserClassFireBase): Result<Unit> {
        return try {
            val response = api.editeProfile(userId, userRequest)
            if (response.isSuccessful && response.body() != null) {
                val userData = UserClass(
                    userId,
                    userRequest.firstname,
                    userRequest.lastname,
                    userRequest.email,
                    userRequest.phonenumber,
                    userRequest.birthday,
                    userRequest.governorate,
                    userRequest.city,
                    userRequest.address
                )
                userdao.addUser(userData)

                Result.success(Unit)
            } else {
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }

                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("خطأ في الشبكة: ${e.message}"))
        }
    }

    fun login(){
        _isLogin.value = true
    }

    fun logout(){
        _isLogin.value = false
    }

    suspend fun validateUserOnAppLaunch(): Boolean {
        return try {
            supabaseUserRemoteDataSource.retrieveUser()
            true
        } catch(e: Exception) {
            clearLocalData()
            false
        }
    }

    suspend fun observeSessionStatus(){
        supabaseUserRemoteDataSource.sessionStatus.collect { status ->
            if(status is SessionStatus.NotAuthenticated){
                clearLocalData()
            }
        }
    }

    private suspend fun clearLocalData(){
        userdao.deleteUserFromDatabase()
        favoriteDao.deleteAllFromFavorite()
    }
}
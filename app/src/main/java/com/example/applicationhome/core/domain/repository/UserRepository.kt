package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.ChickEmailStates
import com.example.applicationhome.data.data.model.FirebasePostResponse
import com.example.applicationhome.data.data.model.SignUpStates
import com.example.applicationhome.data.data.model.UserClassFireBase
import com.example.applicationhome.data.local.dao.UsersDao
import com.example.applicationhome.data.local.entity.UpdateAccountState
import com.example.applicationhome.data.local.entity.UserClass
import com.example.applicationhome.data.remote.FoodAppAPIs
import com.example.applicationhome.domain.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import retrofit2.Response
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
    private val userdao: UsersDao,
    private val api : FoodAppAPIs,
    @ApplicationScope externalScope: CoroutineScope
) {
    private val _isLogin = MutableStateFlow(false)
    val isLogin : StateFlow<Boolean> = _isLogin

    val userData : StateFlow<UserClass> =
        userdao.getActiveUser(true)
            .map { userInDb ->
                userInDb ?: UserClass(firstname = "Guest")
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserClass(firstname = "Guest")
            )


    suspend fun setUserDataToDatabase(emailstate : String, passwordstate : String?): ChickEmailStates {
        try {
            val formatEmail = "\"$emailstate\""
            val response = api.getUserData(order = "\"email\"", value = formatEmail)

            if(response.isSuccessful && response.body() != null){
                val userMap = response.body()
                if(!userMap.isNullOrEmpty()){
                    val user = userMap.values.firstOrNull()
                    if(user != null && passwordstate == user.password){
                        val userId = userMap.keys.firstOrNull().toString()
                        val data = UserClass(
                            userId,
                            user.firstname,
                            user.lastname,
                            user.email,
                            user.password,
                            user.phonenumber,
                            user.birthday,
                            user.governorate,
                            user.city,
                            user.address,
                            isActive = true
                        )
                        userdao.addUser(data)
                        return ChickEmailStates.PasswordTrue(userId)
                    } else {
                        return ChickEmailStates.PasswordFalse
                    }
                } else {
                    return ChickEmailStates.EmailFalse
                }
            } else {
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                return ChickEmailStates.NetworkError(errorMessage)
            }
        } catch (e : Exception){
            return ChickEmailStates.NetworkError("خطأ في الشبكة: ${e.message}")
        }
    }

    suspend fun checkEmailInApi(emailstate : String): ChickEmailStates{
        try {
            val formatEmail = "\"$emailstate\""
            val response = api.getUserData(order = "\"email\"", value = formatEmail)

            if(response.isSuccessful && response.body() != null){
                val userMap = response.body()
                if(!userMap.isNullOrEmpty()){
                    return ChickEmailStates.EmailTrue
                }else{
                    return ChickEmailStates.EmailFalse
                }
            }else{
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                return ChickEmailStates.NetworkError(errorMessage)
            }
        } catch (e : Exception){
            return ChickEmailStates.NetworkError("خطأ في الشبكة: ${e.message}")
        }
    }


    suspend fun logOut(email : String): String{
        return try {
            userdao.updateUser(UpdateAccountState(email, isActive = false))
            "Success"
        } catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun signUp(userRequest : UserClassFireBase): SignUpStates {
        try {
            val response: Response<FirebasePostResponse> = api.signUp(userRequest)
            if (response.isSuccessful && response.body() != null) {
                val userId = response.body()?.name.toString()
                val userData = UserClass(
                    userId,
                    userRequest.firstname,
                    userRequest.lastname,
                    userRequest.email,
                    userRequest.password,
                    userRequest.phonenumber,
                    userRequest.birthday,
                    userRequest.governorate,
                    userRequest.city,
                    userRequest.address,
                    isActive = true
                )
                userdao.addUser(userData)

                return SignUpStates.Success(userId)
            } else {
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }

                return SignUpStates.Error(errorMessage)
            }
        } catch (e: Exception) {
            return SignUpStates.Error("خطأ في الشبكة: ${e.message}")
        }
    }

    fun login(){
        _isLogin.value = true
    }

    fun logout(){
        _isLogin.value = false
    }
}
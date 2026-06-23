package com.example.applicationhome.data.models.repository

import com.example.applicationhome.data.models.local.UpdateAccountState
import com.example.applicationhome.data.models.local.UserClass
import com.example.applicationhome.data.models.local.UsersDao
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.UserClassFireBase
import com.example.applicationhome.data.models.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

//    suspend fun addToMeals(){
//        try {
//            snacks.forEach { (key, value) ->
//                RetrofitInstance.api.addToMeals(key, mapOf("restaurantId" to 0))
//            }
//        }catch (e : Exception){
//            println("")
//        }
//    }


class UserRepository(private val userdao: UsersDao) {
    private val _userId = MutableStateFlow("")
    val userId : StateFlow<String> = _userId.asStateFlow()

    fun getActiveUserFromDatabase() : Flow<UserClass?> = userdao.getActiveUser(true)

    suspend fun setUserDataToDatabase(emailstate : String, passwordstate : String?): String {
        try {
            val formatEmail = "\"$emailstate\""
            val response = RetrofitInstance.api.getUserData(order = "\"email\"", value = formatEmail)

            if(response.isSuccessful && response.body() != null){
                val userMap = response.body()
                if(userMap != null){
                    val user = userMap.values.firstOrNull()
                    if(user != null && passwordstate != null && passwordstate == user.password){
                        _userId.value = userMap.keys.first()
                        val data = UserClass(
                            _userId.value ?: "",
                            user.firstname ?: "",
                            user.lastname ?: "",
                            user.email ?: "",
                            user.password ?: "",
                            user.phonenumber ?: "",
                            user.address ?: "",
                            isActive = true
                        )
                        userdao.addUser(data)
                        return "Password is true"
                    } else {
                        return "Password is false"
                    }
                } else {
                    return "Email is false"
                }
            } else {
                return "Network error"
            }
        } catch (e : Exception){
            return "خطأ في الشبكة: ${e.message}"
        }
    }


    suspend fun logOut(email : String): String{
        return try {
            _userId.value = ""
            userdao.updateUser(UpdateAccountState(email, isActive = false))
            "Success"
        } catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun signUp(userRequest : UserClassFireBase): String {
        try {
            val response: Response<FirebasePostResponse> = RetrofitInstance.api.signUp(userRequest)
            if (response.isSuccessful && response.body() != null) {
                _userId.value = response.body()?.name.toString()
                val userData = UserClass(
                    _userId.value,
                    userRequest.firstname,
                    userRequest.lastname,
                    userRequest.email,
                    userRequest.password,
                    userRequest.phonenumber,
                    userRequest.address,
                    isActive = true
                )
                userdao.addUser(userData)
                return "The operation was successful Account created"
            } else {
                return "The operation failed"
            }
        } catch (e: Exception) {
            return "خطأ في الشبكة: ${e.message}"
        }
    }
}
package com.example.applicationhome.data.models.repository

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.local.UpdateAccountState
import com.example.applicationhome.data.models.local.UserClass
import com.example.applicationhome.data.models.local.UsersDao
import com.example.applicationhome.data.models.local.UsersDatabase
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.UserClassFireBase
import com.example.applicationhome.data.models.remote.RetrofitInstance
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


object UserRepository {
    private lateinit var userdao: UsersDao
    fun initialize(context: Context) {
        userdao = UsersDatabase.getDaoInstance(context).userDao
    }

    var userId by mutableStateOf("")
    var isLogin by mutableStateOf(false)
    var userData by mutableStateOf(UserClass(firstname = "Guest"))

    suspend fun setUserDataToDatabase(emailstate : String, passwordstate : String?): String {
        try {
            val formatEmail = "\"$emailstate\""
            val response = RetrofitInstance.api.getUserData(order = "\"email\"", value = formatEmail)

            if(response.isSuccessful && response.body() != null){
                val userMap = response.body()
                if(userMap != null){
                    val user = userMap.values.firstOrNull()

                    // تأمين الـ Nullability عشان الإيرورز اللي كانت طالعة في الصورة
                    if(user != null && passwordstate != null && passwordstate == user.password){
                        userId = userMap.keys.first()
                        val data = UserClass(
                            userId ?: "",
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

    suspend fun getDataFromDatabase(): UserClass?{
        userdao.updateUser(UpdateAccountState(userData.email, isActive = true))
        return userdao.getOneUser(userId)
    }

    suspend fun getActiveUserFromDatabase(): UserClass?{
        return userdao.getActiveUser()
    }

    suspend fun logOut(): String{
        return try {
            userdao.updateUser(UpdateAccountState(userData.email, isActive = false))
            "Success"
        } catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun signUp(userRequest : UserClassFireBase): String {
        try {
            val response: Response<FirebasePostResponse> = RetrofitInstance.api.signUp(userRequest)
            if (response.isSuccessful && response.body() != null) {
                userId = response.body()?.name.toString()
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
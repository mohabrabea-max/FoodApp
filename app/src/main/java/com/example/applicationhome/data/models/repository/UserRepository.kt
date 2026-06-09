package com.example.applicationhome.data.models.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.UserClass
import com.example.applicationhome.data.models.remote.RetrofitInstance
import retrofit2.Response

object UserRepository {
    var isEmailDone by mutableStateOf(false)
    var userId by mutableStateOf("")
    var isLogin by mutableStateOf(false)
    var userData by mutableStateOf(
        UserClass(
            "Guest",
            null,
            null,
            null,
            null,
            null
        )
    )
//    suspend fun addToMeals(){
//        try {
//            snacks.forEach { (key, value) ->
//                RetrofitInstance.api.addToMeals(key, mapOf("restaurantId" to 0))
//            }
//        }catch (e : Exception){
//            println("")
//        }
//    }

    suspend fun getUserData(emailstate : String, passwordstate : String?): String {
        return try {
            val formatEmail = "\"$emailstate\""
            val response = RetrofitInstance.api.getUserData(order = "\"email\"", value = formatEmail)
            if(response.isSuccessful && response.body() != null){
                val userMap = response.body()!!
                if(userMap.isNotEmpty()){
                    "Email is true"
                    if(passwordstate == userMap.values.firstOrNull()?.password){
                        val user = userMap.values.firstOrNull()
                        userData = userData.copy(
                            user?.firstname,
                            user?.lastname,
                            user?.email,
                            user?.password,
                            user?.phonenumber,
                            user?.address
                        )
                        userId = userMap.keys.first()
                        "Password is true"
                    }else{
                        "Password is false"
                    }
                }else{
                    "Email is false"
                }
            }else{
                "Network error"
            }
        } catch (e : Exception){
            println("خطأ في الشبكة: ${e.message}")
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun signUp(userRequest : UserClass): String {
        return try {
            val response: Response<FirebasePostResponse> = RetrofitInstance.api.signUp(userRequest)
            // 🟢 الـ if الذكية بتاعتك هنا عشان تتأكد إن العملية نجحت
            if (response.isSuccessful && response.body() != null) {
                userId = response.body()?.name.toString()
                "The operation was successful Account created"
            } else {
                "The operation failed"
            }

        } catch (e: Exception) {
            println("خطأ")
            "خطأ في الشبكة: ${e.message}"
        }
    }
}
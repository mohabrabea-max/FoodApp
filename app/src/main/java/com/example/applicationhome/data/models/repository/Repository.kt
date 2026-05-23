package com.example.applicationhome.data.models.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.applicationhome.data.models.model.CartClass
import com.example.applicationhome.data.models.model.FirebasePostResponse
import com.example.applicationhome.data.models.model.UserClass
import com.example.applicationhome.data.models.remote.RetrofitInstance
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.ui.theme.screens.Cart
import retrofit2.Response
import retrofit2.Retrofit

object UserRepository {
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

    suspend fun getUserData(emailstate : String, passwordstate : String?): String {
        return try {
            val formatEmail = "\"$emailstate\""
            val response = RetrofitInstance.api.getAnyData<UserClass>(nodename = "users", order = "\"email\"", value = formatEmail)
            if(response.isSuccessful && response.body() != null){
                val userMap = response.body()!!
                if(userMap.isNotEmpty()){
                    "Email is true"
                    if(passwordstate == userMap.values.first().password){
                        userData = userMap.values.first()
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

object CartRepository {
//    var totalCart by mutableStateOf(0)
    var cartItems by mutableStateOf(CartClass(null, null))
    var totalPrice = mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)

    suspend fun addMealToCart(foodId: Int, size : String, number : Int): String{
        if(cartItems.size.isNullOrEmpty()){
            try {
                val response = RetrofitInstance.api.addToCart("\"Cart\"", "\"$foodId + $size\"", cartItems)
                if(response.isSuccessful){
                    val cartItem = response.body()
                    cartItems = cartItems.copy(cartItem?.size, cartItem?.number)
                }else{
                    "Network error"
                }
            }catch (e : Exception){
                "خطأ في الشبكة: ${e.message}"
            }
        }else{
            try {
                val updatesMap = mapOf("number" to number)
                val response = RetrofitInstance.api.updateQuantity("\"Cart\"", "\"$foodId + $size\"", updatesMap)

                if(response.isSuccessful && response.body()!= null){

                }else{
                    "Network error"
                }
            }catch (e : Exception){
                "خطأ في الشبكة: ${e.message}"
            }
        }
    }

}
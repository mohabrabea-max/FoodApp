package com.example.applicationhome.data.models.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{
    private const val BASE_URL = "https://food-app-9d163-default-rtdb.firebaseio.com/food_app/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // السطر دا عشان نحول ملف الJSON لداتا كلاس كوتلن
            .build()
    }
    val api : FoodAppAPIs by lazy {   //   الفاليو اللي هنستخدمه عشان نستدعي الداتا
        retrofit.create(FoodAppAPIs::class.java)
    }
}

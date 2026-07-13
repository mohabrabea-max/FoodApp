package com.example.applicationhome.data.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance{
    private const val BASE_URL = "https://food-app-9d163-default-rtdb.firebaseio.com/food_app/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // السطر دا عشان نحول ملف الJSON لداتا كلاس كوتلن
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodAppApi(retrofit: Retrofit): FoodAppAPIs {   //   الفاليو اللي هنستخدمه عشان نستدعي الداتا
        return retrofit.create(FoodAppAPIs::class.java)
    }
}

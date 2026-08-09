package com.example.applicationhome.data.remote.paymob

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {
    private val BASE_URL = "https://accept.paymob.com/api/"

    @Provides
    @Singleton
    fun providePaymobApiService(): PaymobApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymobApiService::class.java)
    }
}
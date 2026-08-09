package com.example.applicationhome.data.remote.paymob

import com.example.applicationhome.data.data.model.PaymobAuthRequest
import com.example.applicationhome.data.data.model.PaymobAuthResponse
import com.example.applicationhome.data.data.model.PaymobOrderRequest
import com.example.applicationhome.data.data.model.PaymobOrderResponse
import com.example.applicationhome.data.data.model.PaymobPaymentKeyRequest
import com.example.applicationhome.data.data.model.PaymobPaymentKeyResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymobApiService {
    // 1. طلب التوكن
    @POST("auth/tokens")
    suspend fun getAuthToken(
        @Body request : PaymobAuthRequest
    ): PaymobAuthResponse

    // 2. تسجيل الطلب
    @POST("ecommerce/orders")
    suspend fun createOrder(
        @Body request: PaymobOrderRequest
    ): PaymobOrderResponse

    // 3. طلب مفتاح الدفع
    @POST("acceptance/payment_keys")
    suspend fun getPaymentKey(
        @Body request: PaymobPaymentKeyRequest
    ): PaymobPaymentKeyResponse
}
package com.example.applicationhome.core.data.remote.paymob

import com.example.applicationhome.core.domain.model.PaymobAuthRequest
import com.example.applicationhome.core.domain.model.PaymobAuthResponse
import com.example.applicationhome.core.domain.model.PaymobOrderRequest
import com.example.applicationhome.core.domain.model.PaymobOrderResponse
import com.example.applicationhome.core.domain.model.PaymobPaymentKeyRequest
import com.example.applicationhome.core.domain.model.PaymobPaymentKeyResponse
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
package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.BuildConfig
import com.example.applicationhome.core.domain.repository.PaymobRepository
import com.example.applicationhome.data.data.model.PaymobAuthRequest
import com.example.applicationhome.data.data.model.PaymobBillingData
import com.example.applicationhome.data.data.model.PaymobOrderRequest
import com.example.applicationhome.data.data.model.PaymobPaymentKeyRequest
import com.example.applicationhome.data.remote.paymob.PaymobApiService
import javax.inject.Inject

class PaymobRepositoryImpl @Inject constructor(
    private val apiService : PaymobApiService
): PaymobRepository {
    override suspend fun getAuthToken(): Result<String> {
        return try {
            val request = PaymobAuthRequest(apiKey = BuildConfig.PAYMOB_API_KEY)
            val response = apiService.getAuthToken(request)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createOrder(
        authToken : String,
        amountCents : String
    ): Result<String> {
        return try {
            val request = PaymobOrderRequest(
                authToken = authToken,
                amountCents = amountCents
            )
            val response = apiService.createOrder(request)
            Result.success(response.id.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPaymentKey(
        authToken : String,
        orderId : String,
        amountCents : String,
        billingData : PaymobBillingData,
        integrationId : Int
    ): Result<String> {
        return try {
            val request = PaymobPaymentKeyRequest(
                authToken = authToken,
                amountCents = amountCents,
                orderId = orderId,
                billingData = billingData,
                integrationId = integrationId
            )
            val response = apiService.getPaymentKey(request)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
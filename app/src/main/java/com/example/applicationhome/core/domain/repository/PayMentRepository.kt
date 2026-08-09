package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.PaymobBillingData

interface PaymobRepository {
    suspend fun getAuthToken(): Result<String>
    suspend fun createOrder(authToken: String, amountCents: String): Result<String>
    suspend fun getPaymentKey(
        authToken: String,
        orderId: String,
        amountCents: String,
        billingData: PaymobBillingData,
        integrationId: Int
    ): Result<String>
}
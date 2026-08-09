package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.PaymobRepository
import com.example.applicationhome.data.data.model.PaymobBillingData
import javax.inject.Inject

class PaymentUseCase @Inject constructor(
    private val paymobRepository : PaymobRepository
){
    suspend operator fun invoke(
        orderPrice : Double,
        billingData : PaymobBillingData,
        integrationId : Int
    ): Result<String> {
        val amountCents = (orderPrice * 100).toInt().toString()

        // 1. طلب التوكن
        val authResult = paymobRepository.getAuthToken()
        val authToken = authResult.getOrElse { return Result.failure(it) }

        // 2. إنشاء الطلب
        val orderResult = paymobRepository.createOrder(authToken, amountCents)
        val orderId = orderResult.getOrElse { return Result.failure(it) }

        // 3. طلب مفتاح الدفع النهائي
        return paymobRepository.getPaymentKey(
            authToken = authToken,
            orderId = orderId,
            amountCents = amountCents,
            billingData = billingData,
            integrationId = integrationId
        )
    }
}
package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.ActionsStates
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val userRepository : UserRepository,
    private val orderRepository : OrderRepository
){
    suspend operator fun invoke(orderId : Long): ActionsStates {
        val userId = userRepository.userData.firstOrNull()?.id
            ?: return ActionsStates.Failed("Unexpected error")

        return orderRepository.cancelOrder(
            userId = userId,
            orderId = orderId,
            index = 3
        ).fold(
            onSuccess = { ActionsStates.Success },
            onFailure = { ActionsStates.Failed("Unexpected error") }
        )
    }
}
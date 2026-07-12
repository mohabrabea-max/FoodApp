package com.example.applicationhome.domain

import com.example.applicationhome.data.data.local.entity.CartClass
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    @field:ApplicationScope private val externalScope: CoroutineScope
){
    val cartInformation: StateFlow<CartClass?> =
        userRepository.userData
            .flatMapLatest { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    cartRepository.getCartData(id)
                }else {
                    flowOf(null)
                }
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val cartItems: StateFlow<List<CartItemsClass?>> =
        userRepository.userData
            .flatMapLatest { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    cartRepository.getCartItems(id)
                } else {
                    flowOf(emptyList())
                }
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val totalNumber: StateFlow<Int> =
        cartItems
            .map { item -> item.sumOf { it?.quantity ?: 0 } }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )
}
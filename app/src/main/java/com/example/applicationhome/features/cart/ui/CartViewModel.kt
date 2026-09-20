package com.example.applicationhome.features.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.model.CategoryEnum
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.ui.mapper.cartItemsDomainClassToCartItemsUiClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    cartRepository: CartRepository,
    private val userRepository: UserRepository
): ViewModel(){
    val cartItems = userRepository.userData
        .flatMapLatest { user ->
            val id = user.id
            if (id.isNotEmpty()) {
                cartRepository.getCartItems(id).map { items ->
                    items.mapNotNull {
                        it?.cartItemsDomainClassToCartItemsUiClass()
                    }
                }
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val totalPrice = cartRepository.totalPrice


    fun plus(mealId : Int, size : String, type : CategoryEnum){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.plus(userId, mealId, size, type)
        }
    }

    fun minus(mealId : Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, mealId, size)
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }
}
package com.example.applicationhome.features.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.data.local.entity.CartItemsClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    cartRepository: CartRepository,
    private val userRepository: UserRepository,
    orderRepository: OrderRepository
): ViewModel(){
    val loading : StateFlow<Boolean> = orderRepository.loading

    val errorInCart = MutableStateFlow(false)

    val cartInformation = cartRepository.cartInformation

    val cartItems = cartRepository.cartItems

    val totalNumber = cartRepository.totalNumber

    val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    val newFoodInCartSize = MutableStateFlow<String?>(null)

    val totalPrice = cartRepository.totalPrice



    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)
            if(state != null){
                alertDialogTrue()
                newFoodInCartSize.value = state.first
                newFoodInCart.value = state.second
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, food, size)
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }


    fun alertDialogTrue(){
        errorInCart.value = true
    }
}
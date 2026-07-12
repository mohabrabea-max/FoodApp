package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel @Inject constructor(
    userRepository: UserRepository,
    getCartUseCase: GetCartUseCase,
    private val cartRepository: CartRepository
): ViewModel(){

    var errorInCart by mutableStateOf(false)


    val userid: StateFlow<String> = userRepository.userData
        .map { item -> item.id }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val cartInformation = getCartUseCase.cartInformation

    val cartItems = getCartUseCase.cartItems

    var cartRestaurant by mutableStateOf(Restaurants())
    var activId by mutableStateOf<Int?>(null)

    var newFoodInCart by mutableStateOf<CartItemsClass?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)
    var newCount by mutableStateOf(0)
    var totalPrice by mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)



    init {
        viewModelScope.launch (Dispatchers.IO){
            cartItems.collect { cartList ->
                updateTotals(cartList)
            }
        }
    }

    fun plus(food: CartItemsClass, size : String, onError: (String) -> Unit = {}){
        viewModelScope.launch(Dispatchers.IO) {
            val mealKey = "${food.mealId}_${size}"
            val currentItem = cartItems.value.find { it?.mealKey == mealKey }
            val finalNumber = if (currentItem != null){
                if(currentItem.quantity == 99){
                    99
                }else{
                    currentItem.quantity + 1
                }
            }else{
                1
            }
            if(cartItems.value.isNotEmpty()){
                val currentCart = cartInformation.filterNotNull().first()
                if(food.restaurantId == currentCart.restaurantId){
                    if(cartItems.value.find { it?.mealKey == mealKey } != null){
                        cartRepository.updateQuantity(userid.value, food, size, food.priceOfOne, finalNumber)
                    }else{
                        cartRepository.addMealToCart(userid.value, food, size, food.type, food.priceOfOne, finalNumber)
                    }
                }else{
                    withContext(Dispatchers.Main){
                        onError("ERROR")
                    }
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid.value, food, size, food.type, food.priceOfOne, cartRestaurant, finalNumber)
            }
        }
    }

    fun updateCount(food : CartItemsClass, size : String, newCount : Int, onError: (String) -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            val mealKey = "${food.mealId}_${size}"
            val cartItem = cartItems.value.find { it?.mealKey == mealKey }

            if(cartItems.value.isNotEmpty()){
                val currentCart = cartInformation.filterNotNull().first()
                if(food.restaurantId == currentCart.restaurantId){
                    if(cartItem != null){
                        val finalNumber =
                            if(cartItem.quantity + newCount > 99){
                                99
                            }else{
                                cartItem.quantity + newCount
                            }
                        cartRepository.updateQuantity(userid.value, food, size, food.priceOfOne, finalNumber)
                    }else{

                        cartRepository.addMealToCart(userid.value, food, size, food.type, food.priceOfOne, newCount)
                    }
                }else{
                    withContext(Dispatchers.Main){
                        onError("ERROR")
                    }
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid.value, food, size, food.type, food.priceOfOne, cartRestaurant, newCount)
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
            val mealKey = "${food.mealId}_${size}"
            var finalNumber by mutableStateOf(0)
            val cartItem = cartItems.value.find { it?.mealKey == mealKey }
            if(cartItem != null){
                if(cartItem.quantity == 1){
                    cartRepository.deleteFromCart(userid.value, food.mealId, size)
                }else{
                    finalNumber = cartItem.quantity - 1
                    cartRepository.updateQuantity(userid.value, food, size, food.priceOfOne, finalNumber)
                }
            }
        }
    }

    fun clearAndStartNewCart(count : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            totalPrice = 0.0
            totalNumber.value = 0
            val newFood = newFoodInCart
            val newSize = newFoodInCartSize
            cartRepository.deleteAllCart(userid.value)
            cartRepository.deleteParentCart(userid.value)

            if(newFood != null && newSize != null){
                updateCount(newFood, newSize, count)
                deletenewCount()
            }

            newFoodInCart = null
            newFoodInCartSize = null
        }
    }

    fun clearAllCart(){
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAllCart(userid.value)
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            cartRepository.deleteFromCart(userid.value, foodId, size)
        }
    }

    fun updateTotals(cartItems : List<CartItemsClass?>) {
        totalNumber.value = 0
        totalPrice = 0.0
        cartItems.forEach { item ->
            totalPrice += item?.totalPrice ?: 0.0
            totalNumber.value += item?.quantity ?: 0
        }
    }

    fun active(foodId : Int){
        activId = foodId
    }

    fun alertDialogTrue(){
        errorInCart = true
    }

    fun alertDialogFalse(){
        errorInCart = false
    }

    fun plusnewCount(){
        newCount += 1
    }

    fun minusnewCount(){
        newCount -= 1
    }

    fun deletenewCount(){
        newCount = 0
    }
}
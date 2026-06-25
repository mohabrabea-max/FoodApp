package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.local.CartClass
import com.example.applicationhome.data.models.local.CartItemsClass
import com.example.applicationhome.data.models.model.CartClassForCalculations
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.data.models.repository.OrderRepository
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository : OrderRepository
) : ViewModel(){

    var userid by mutableStateOf("")
    val cartInformation : StateFlow<CartClass?> = userRepository.userId
        .flatMapLatest { id ->
            if (id.isNotEmpty()) {
                userid = id
                cartRepository.getCartData(id)
            }else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val cartItems : StateFlow<List<CartItemsClass?>> = userRepository.userId
        .flatMapLatest { id ->
            if (id.isNotEmpty()) {
                userid = id
                cartRepository.getCartItems(id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var cartRestaurant by mutableStateOf(Restaurants())
    var activId by mutableStateOf<Int?>(null)
    var errorInCart by mutableStateOf(false)
    var newFoodInCart by mutableStateOf<CartClassForCalculations?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)
    var newCount by mutableStateOf(0)
    var totalPrice by mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)

    var meal : FoodItem? by mutableStateOf(null)


    init {
        viewModelScope.launch {
            cartItems.collect { cartList ->
                updateTotals(cartList)
            }
        }
    }

    fun plus(food: CartClassForCalculations, size : String){
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
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
                if(food.restaurantId == cartInformation.value?.restaurantId){
                    if(cartItems.value.find { it?.mealKey == mealKey } != null){
                        cartRepository.updateQuantity(userid, food, size, food.type, food.price, finalNumber)
                    }else{
                        cartRepository.addMealToCart(userid, food, size, food.type, food.price, finalNumber)
                    }
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid, food, size, food.type, food.price, cartRestaurant)
            }
        }
    }

    fun updateCount(food : CartClassForCalculations, size : String, newCount : Int) {
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
            val cartItem = cartItems.value.find { it?.mealKey == mealKey }
            val finalNumber = if (cartItem != null){
                if(cartItem.quantity == 99){
                    99
                }else{
                    cartItem.quantity + newCount
                }
            }else{
                1
            }
            if(cartItems.value.isNotEmpty()){
                if(food.restaurantId == cartInformation.value?.restaurantId){
                    if(cartItems.value.find { it?.mealKey == mealKey } != null){
                        cartRepository.updateQuantity(userid, food, size, food.type, food.price, finalNumber)
                    }else{
                        cartRepository.addMealToCart(userid, food, size, food.type, food.price, finalNumber)
                    }
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid, food, size, food.type, food.price, cartRestaurant)
            }
        }
    }

    fun minus(food: CartClassForCalculations, size : String){
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
            var finalNumber by mutableStateOf(0)
            val cartItem = cartItems.value.find { it?.mealKey == mealKey }
            if(cartItem != null){
                if(cartItem.quantity == 1){
                    cartRepository.deleteFromCart(userid, food.id, size)
                }else{
                    finalNumber = cartItem.quantity - 1
                    cartRepository.updateQuantity(userid, food, size, food.type, food.price, finalNumber)
                }
            }
        }
    }

    fun clearAndStartNewCart() {
        viewModelScope.launch {
            totalPrice = 0.0
            totalNumber.value = 0
            val newFood = newFoodInCart
            val newSize = newFoodInCartSize
            cartRepository.deleteAllCart(userid)
            cartRepository.deleteParentCart(userid)
            if(newFood != null && newSize != null){
                plus(newFood, newSize)
            }
            newFoodInCart = null
            newFoodInCartSize = null
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            cartRepository.deleteFromCart(userid, foodId, size)
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

    fun getMeal(mealId : Int){
        viewModelScope.launch {
            meal = cartRepository.getMeal(mealId)
        }
    }
}
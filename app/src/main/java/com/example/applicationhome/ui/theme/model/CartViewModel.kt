package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.local.entity.CartClass
import com.example.applicationhome.data.models.local.entity.CartItemsClass
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.data.models.repository.OrderRepository
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel(
    userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository : OrderRepository
) : ViewModel(){

    var userid by mutableStateOf("")
    val cartInformation : StateFlow<CartClass?> = userRepository.getActiveUserFromDatabase()
        .flatMapLatest { user ->
            val id = user?.id ?: ""
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

    val cartItems : StateFlow<List<CartItemsClass?>> = userRepository.getActiveUserFromDatabase()
        .flatMapLatest { user ->
            val id = user?.id ?: ""
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

    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val dbItems = cartRepository.getCartItems(userid).first()
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
            if(dbItems.isNotEmpty()){
                val currentCart = cartInformation.filterNotNull().first()
                if(food.restaurantId == currentCart.restaurantId){
                    if(cartItems.value.find { it?.mealKey == mealKey } != null){
                        cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
                    }else{
                        cartRepository.addMealToCart(userid, food, size, food.type, food.priceOfOne, finalNumber)
                    }
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, finalNumber)
            }
        }
    }

    fun updateCount(food : CartItemsClass, size : String, newCount : Int) {
        viewModelScope.launch {
            val dbItems = cartRepository.getCartItems(userid).first()
            val mealKey = "${food.mealId}_${size}"
            val cartItem = cartItems.value.find { it?.mealKey == mealKey }

            if(dbItems.isNotEmpty()){
                val currentCart = cartInformation.filterNotNull().first()
                if(food.restaurantId == currentCart.restaurantId){
                    if(cartItem != null){
                        val finalNumber =
                            if(cartItem.quantity + newCount > 99){
                                99
                            }else{
                                cartItem.quantity + newCount
                            }
                        cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
                    }else{

                        cartRepository.addMealToCart(userid, food, size, food.type, food.priceOfOne, newCount)
                    }
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                cartRepository.createNewCart(userid, food, size, food.type, food.priceOfOne, cartRestaurant, newCount)
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val mealKey = "${food.mealId}_${size}"
            var finalNumber by mutableStateOf(0)
            val cartItem = cartItems.value.find { it?.mealKey == mealKey }
            if(cartItem != null){
                if(cartItem.quantity == 1){
                    cartRepository.deleteFromCart(userid, food.mealId, size)
                }else{
                    finalNumber = cartItem.quantity - 1
                    cartRepository.updateQuantity(userid, food, size, food.priceOfOne, finalNumber)
                }
            }
        }
    }

    fun clearAndStartNewCart(count : Int) {
        viewModelScope.launch {
            totalPrice = 0.0
            totalNumber.value = 0
            val newFood = newFoodInCart
            val newSize = newFoodInCartSize
            cartRepository.deleteAllCart(userid)
            cartRepository.deleteParentCart(userid)

            if(newFood != null && newSize != null){
                updateCount(newFood, newSize, count)
                deletenewCount()
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
}
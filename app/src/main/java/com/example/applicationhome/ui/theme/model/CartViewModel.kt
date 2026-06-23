package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.local.CartClass
import com.example.applicationhome.data.models.local.CartItemsClass
import com.example.applicationhome.data.models.model.Food
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.data.models.repository.OrderRepository
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val orderRepository : OrderRepository
) : ViewModel(){
    val cartItems : StateFlow<List<CartItemsClass?>> =
        cartRepository.getCartItems().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val cartInformation : StateFlow<CartClass?> =
        cartRepository.getCartData().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    var cartRestaurant by mutableStateOf(Restaurants())
    var activId by mutableStateOf<Int?>(null)
    var errorInCart by mutableStateOf(false)
    var newFoodInCart by mutableStateOf<Food?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)
    var newCount by mutableStateOf(0)
    var totalPrice by mutableDoubleStateOf(0.0)
    var totalNumber = mutableStateOf(0)

    var meal : FoodItem? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            userRepository.userId.collect { id ->
                cartRepository.setUserId(id)
                orderRepository.setUserId(id)
            }
        }
    }

    init {
        viewModelScope.launch {
            cartItems.collect { cartList ->
                updateTotals(cartList)
            }
        }
    }

    fun plus(food: Food, size : String){
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
            val price : Double? = when(food){
                is FoodItem -> { food.sizeOptions.find { it.size == size }?.price }
                is Snack -> { food.priceANDsize[size] }
            }
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
            val type = when(food){
                is FoodItem -> {"Meal"}
                is Snack -> {"Snack"}
            }
            if(cartItems.value.isNotEmpty()){
                if(food.restaurantId == cartInformation.value?.restaurantId && price != null){
                    if(cartItems.value.find { it?.mealKey == mealKey } != null){
                        cartRepository.updateQuantity(food, size, type, price, finalNumber)
                    }else{
                        cartRepository.addMealToCart(food, size, type, price, finalNumber)
                    }
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                if(price != null){
                    cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                    cartRepository.createNewCart(food, size, type, price, cartRestaurant)
                }
            }
        }
    }

    fun updateCount(food : Food, size : String, newCount : Int) {
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
            val price : Double? = when(food){
                is FoodItem -> { food.sizeOptions.find { it.size == size }?.price }
                is Snack -> { food.priceANDsize[size] }
            }
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
            val type = when(food){
                is FoodItem -> {"Meal"}
                is Snack -> {"Snack"}
            }
            if(cartItems.value.isNotEmpty()){
                if(food.restaurantId == cartInformation.value?.restaurantId && price != null){
                    if(cartItems.value.find { it?.mealKey == mealKey } != null){
                        cartRepository.updateQuantity(food, size, type, price, finalNumber)
                    }else{
                        cartRepository.addMealToCart(food, size, type, price, finalNumber)
                    }
                }else{
                    alertDialogTrue()
                    newFoodInCart = food
                    newFoodInCartSize = size
                }
            }else{
                if(price != null){
                    cartRestaurant = cartRepository.getCartRestaurantData(food)?: Restaurants()
                    cartRepository.createNewCart(food, size, type, price, cartRestaurant)
                }
            }
        }
    }

    fun minus(food: Food, size : String){
        viewModelScope.launch {
            val mealKey = "${food.id}_${size}"
            var finalNumber by mutableStateOf(0)
            val cartItem = cartItems.value.find { it?.mealKey == mealKey }
            val type = when(food){
                is FoodItem -> {"Meal"}
                is Snack -> {"Snack"}
            }
            val price : Double? = when(food){
                is FoodItem -> { food.sizeOptions.find { it.size == size }?.price }
                is Snack -> { food.priceANDsize[size] }
            }
            if(cartItem != null && price != null){
                if(cartItem.quantity == 1){
                    cartRepository.deleteFromCart(food.id, size)
                }else{
                    finalNumber = cartItem.quantity - 1
                    cartRepository.updateQuantity(food, size, type, price, finalNumber)
                }
            }
        }
    }

    fun clearAndStartNewCart() {
        viewModelScope.launch {
            cartRepository.deleteAllCart()
            totalPrice = 0.0
            totalNumber.value = 0
            val newFood = newFoodInCart
            val newSize = newFoodInCartSize
            if(newFood != null && newSize != null){
                plus(newFood, newSize)
            }
            newFoodInCart = null
            newFoodInCartSize = null
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            cartRepository.deleteFromCart(foodId, size)
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
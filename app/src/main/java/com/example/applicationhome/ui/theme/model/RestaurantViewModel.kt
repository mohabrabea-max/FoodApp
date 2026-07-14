package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.model.Drink
import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.data.model.Snack
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.HomeScreenRepository
import com.example.applicationhome.data.data.repository.RestaurantScreenRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.CartUseCase
import com.example.applicationhome.domain.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val networkObserver: NetworkObserver,
    homeScreenRepository: HomeScreenRepository,
    cartRepository: CartRepository,
    private val restaurantScreenRepository : RestaurantScreenRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase
): ViewModel(){

    var isNetworkAvailable by mutableStateOf(false)

    val userData = userRepository.userData


//    *** ---------------------------- \\***  Restaurant Items  ***// ---------------------------- ***


    var resid by mutableStateOf(0)
    var selectedTypeIndex by mutableStateOf(0)
    var typeInRestaurantScreen by mutableStateOf("")

    val restaurantCount = homeScreenRepository.restaurantCount


    private val _foodMenuMap = mutableStateMapOf<String, FoodItem>()
    val foodMenuList = derivedStateOf {
        _foodMenuMap.filter { it.value.restaurantId == resid && it.value.category == typeInRestaurantScreen }.values.toList()
    }
    val foodMenuListIsLoading : StateFlow<Boolean> = restaurantScreenRepository.foodMenuListIsLoading


    private val _snackMenuMap = mutableStateMapOf<String, Snack>()
    val snackMenuList = derivedStateOf {
        _snackMenuMap.filter { it.value.restaurantId == resid }.values.toList()
    }
    val snacksIsLoading : StateFlow<Boolean> = restaurantScreenRepository.snacksIsLoading


    private val _drinkMenuMap = mutableStateMapOf<String, Drink>()
    val drinkMenuList = derivedStateOf {
        _drinkMenuMap.filter { it.value.restaurantId == resid }.values
    }
    val drinkMenuIsLoading : StateFlow<Boolean> = restaurantScreenRepository.drinkMenuIsLoading


    val _restaurantOffersMenuList = mutableStateMapOf<String, Offers>()
    val restaurantOffersMenuList = derivedStateOf {
        _restaurantOffersMenuList.filter { it.value.restaurantId == resid }.values.toList()
    }
    val restaurantOffersLoading : StateFlow<Boolean> = restaurantScreenRepository.restaurantOffersLoading



    fun loadRestaurantId(resId : Int){
        resid = resId
    }

    fun deleteRestaurantId(resId : Int){
        resid = 0
    }

    fun restaurantData(){
        val restaurantscount = restaurantCount[resid]
        if(restaurantscount != null){
            viewModelScope.launch {
                if(foodMenuList.value.size < restaurantscount.meals){
                    val foodMenu = restaurantScreenRepository.uploadFoodMenuFromApi(resid)
                    _foodMenuMap += foodMenu
                }
                if(snackMenuList.value.size < restaurantscount.snacks){
                    val snackMenu = restaurantScreenRepository.uploadSnacksMenuFromApi(resid)
                    _snackMenuMap += snackMenu
                }
                if(restaurantOffersMenuList.value.size < restaurantscount.offers){
                    _restaurantOffersMenuList += restaurantScreenRepository.uploadRestaurantOffersFromApi(resid)
                }
            }
        }
    }

    fun selectedTypeInFavoriteScreen(index : Int, restaurantId : Int){
        viewModelScope.launch {
            val restaurant = favoriteRepository.getRestaurantToView(restaurantId)
            selectedTypeIndex = index
            typeInRestaurantScreen = restaurant?.typ?.toList()?.first() ?: ""
        }
    }

    fun selectedtype(index : Int, type : String){
        selectedTypeIndex = index
        typeInRestaurantScreen = type
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    var errorInCart by mutableStateOf(false)

    val cartInformation = cartRepository.cartInformation

    val cartItems = cartRepository.cartItems

    val totalNumber = cartRepository.totalNumber

    var totalPrice by mutableDoubleStateOf(0.0)

    var newCount by mutableStateOf(0)

    var newFoodInCart by mutableStateOf<CartItemsClass?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)



    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }
        }
        viewModelScope.launch (Dispatchers.IO){
            cartItems.collect { cartList ->
                updateTotals(cartList)
            }
        }
    }


    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)
            if(state != null){
                alertDialogTrue()
                newFoodInCartSize = state.first
                newFoodInCart = state.second
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, food, size)
        }
    }

    fun clearAndStartNewCart(count : Int) {
        viewModelScope.launch {
            totalPrice = 0.0
            val newFood = newFoodInCart
            val newSize = newFoodInCartSize
            val userId = userRepository.userData.value.id
            val finally = cartUseCase.clearAndStartNewCart(userId, newFoodInCart, newFoodInCartSize)

            if(finally && newFood != null && newSize != null){
                cartUseCase.updateCount(userId, newFood, newSize, count)
                deletenewCount()
                newFoodInCart = null
                newFoodInCartSize = null
            }
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }

    fun updateTotals(cartItems : List<CartItemsClass?>) {
        totalPrice = 0.0
        cartItems.forEach { item ->
            totalPrice += item?.totalPrice ?: 0.0
        }
    }

    fun quantity(snackKey : String) = cartItems.value.find { it?.mealKey == snackKey }?.quantity ?: 0

    fun deletenewCount(){
        newCount = 0
    }

    fun alertDialogTrue(){
        errorInCart = true
    }

    fun alertDialogFalse(){
        errorInCart = false
    }


    //       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addMealFavorite(food : FavoriteFoodDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addMealFavorite(food)
        }
    }
    fun addSnackFavorite(snack : FavoriteSnacksDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addSnackFavorite(snack)
        }
    }
    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addRestaurantsFavorite(restaurants)
        }
    }


    fun removeMealFavorite(mealId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeMealFavorite(mealId)
        }
    }
    fun removeSnackFavorite(snackId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeSnackFavorite(snackId)
        }
    }
    fun removeRestaurantsFavorite(resId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeRestaurantsFavorite(resId)
        }
    }


    fun isMealInFavorite(foodId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isMealInFavorite(foodId)
    }
    fun isSnackInFavorite(snackId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isSnackInFavorite(snackId)
    }
    fun isRestaurantInFavorite(resId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isRestaurantInFavorite(resId)
    }
}
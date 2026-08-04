package com.example.applicationhome.features.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.ItemScreenRepository
import com.example.applicationhome.core.domain.repository.RestaurantScreenRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModel @Inject constructor(
    cartRepository : CartRepository,
    private val userRepository : UserRepository,
    private val favoriteRepository : FavoriteRepository,
    private val itemScreenRepository : ItemScreenRepository,
    private val restaurantScreenRepository : RestaurantScreenRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase,
    private val networkObserver : NetworkObserver
) : ViewModel(){

    val userData = userRepository.userData


//        *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    val selectedCategorieInFavoriteScreen = MutableStateFlow(0)

    val favoriteMeals = favoriteRepository.favoriteMeals

    val favoriteSnacks = favoriteRepository.favoriteSnacks

    val favoriteRestaurantsFromDatabase = favoriteRepository.favoriteRestaurantsFromDatabase

    val favoriteFoodCount = favoriteRepository.favoriteFoodCount

    val favoriteSnacksCount = favoriteRepository.favoriteSnacksCount

    val favoriteRestaurantsCount = favoriteRepository.favoriteRestaurantsCount

    val isNetworkAvailable = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable.value = available
            }
        }
    }


    fun addMealFavorite(food : FavoriteMealEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addMealFavorite(food)
        }
    }
    fun addSnackFavorite(snack : FavoriteSnackEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addSnackFavorite(snack)
        }
    }
    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantEntity){
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


    fun selectedFavoriteScreen(index: Int){
        selectedCategorieInFavoriteScreen.value = index
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    var errorInCart = MutableStateFlow(Pair(false,""))


    val cartItems = cartRepository.cartItems
    val cartInformation = cartRepository.cartInformation


    val totalPrice = cartRepository.totalPrice

    val newCount = MutableStateFlow(0)

    val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    val newFoodInCartSize = MutableStateFlow<String?>(null)



    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)
            if(state != null){
                if(state.first == "User Id Is Empty"){
                    alertDialogTrue(Pair(true, "User Id Is Empty"))
                }else{
                    alertDialogTrue(Pair(true, "Error In Restaurant"))
                    newFoodInCartSize.value = state.first
                    newFoodInCart.value = state.second
                }
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
            val newFood = newFoodInCart.value
            val newSize = newFoodInCartSize.value
            val userId = userRepository.userData.value.id
            val finally = cartUseCase.clearAndStartNewCart(userId, newFoodInCart.value, newFoodInCartSize.value)

            if(finally && newFood != null && newSize != null){
                cartUseCase.updateCount(userId, newFood, newSize, count)
                deletenewCount()
                newFoodInCart.value = null
                newFoodInCartSize.value = null
            }
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }

    fun deletenewCount(){
        newCount.value = 0
    }

    private fun alertDialogTrue(error : Pair<Boolean, String>){
        errorInCart.value = error
    }

    fun alertDialogFalse(){
        errorInCart.value = Pair(false,"")
    }
}
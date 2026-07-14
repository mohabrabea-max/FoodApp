package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.CartUseCase
import com.example.applicationhome.domain.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModel @Inject constructor(
    cartRepository : CartRepository,
    private val userRepository : UserRepository,
    private val favoriteRepository : FavoriteRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase,
    private val networkObserver : NetworkObserver
) : ViewModel(){

    var isNetworkAvailable by mutableStateOf(false)

    val userData = userRepository.userData


//        *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    var selectedCategorieInFavoriteScreen by mutableIntStateOf(0)

    val favoriteMeals = favoriteRepository.favoriteMeals

    val favoriteSnacks = favoriteRepository.favoriteSnacks

    val favoriteRestaurantsFromDatabase = favoriteRepository.favoriteRestaurantsFromDatabase

    val favoriteFoodCount = favoriteRepository.favoriteFoodCount

    val favoriteSnacksCount = favoriteRepository.favoriteSnacksCount

    val favoriteRestaurantsCount = favoriteRepository.favoriteRestaurantsCount



    init {
        viewModelScope.launch {
            userRepository.userData.collect { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    networkObserver.isNetworkAvailable.collect { available ->
                        isNetworkAvailable = available
                        if (available) {
                            favoriteRepository.syncFavoritesInDatabase(id)
                        }
                    }
                }
            }
        }
    }


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


    fun selectedFavoriteScreen(index: Int){
        selectedCategorieInFavoriteScreen = index
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    var errorInCart by mutableStateOf(false)


    val cartItems = cartRepository.cartItems


    var totalPrice by mutableDoubleStateOf(0.0)

    var newCount by mutableStateOf(0)

    var newFoodInCart by mutableStateOf<CartItemsClass?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)



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

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }

    fun quantity(snackKey : String) = cartItems.value.find { it?.mealKey == snackKey }?.quantity ?: 0

    fun deletenewCount(){
        newCount = 0
    }

    fun alertDialogTrue(){
        errorInCart = true
    }
}
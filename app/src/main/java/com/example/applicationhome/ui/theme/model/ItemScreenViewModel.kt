package com.example.applicationhome.ui.theme.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.CartUseCase
import com.example.applicationhome.domain.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemScreenViewModel @Inject constructor(
    private val favoriteRepository : FavoriteRepository,
    val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val cartUseCase: CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase
) : ViewModel() {

    val userData = userRepository.userData



//       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    var selectedSnak by mutableStateOf<FavoriteSnacksDatabase?>(null)
    var selectedSnackSize by mutableStateOf("Small")
    var selectedItem by mutableStateOf<FavoriteFoodDatabase?>(null)
    var selectedSize by mutableStateOf("Small")
    var selectedRestaurant by mutableStateOf<Restaurants?>(null)


    fun selectItem(item: FavoriteFoodDatabase, size : String) {
        selectedItem = item
        selectedSize = size
    }
    fun selectSnak(item: FavoriteSnacksDatabase?, size : String){
        selectedSnak = item
        selectedSnackSize = size
    }
    fun selectRestaurant(item : Restaurants){
        selectedRestaurant = item
        viewModelScope.launch {
            try {
                val newData = favoriteRepository.getRestaurantToView(item.id)
                selectedRestaurant = newData
            }catch (e : Exception){
                Log.e("SelectRestaurant", "Error fetching fresh data", e)
            }
        }
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    val cartInformation = cartRepository.cartInformation

    var totalPrice by mutableDoubleStateOf(0.0)

    var errorInCart by mutableStateOf(false)

    var newCount by mutableStateOf(0)

    var newFoodInCart by mutableStateOf<CartItemsClass?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)


    fun updateCount(food : CartItemsClass, size : String, newCount : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.updateCount(userId, food, size, newCount)
            if(state != null){
                alertDialogTrue()
                newFoodInCartSize = state.first
                newFoodInCart = state.second
            }
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

    fun deletenewCount(){
        newCount = 0
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


    //       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addMealFavorite(food : FavoriteFoodDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addMealFavorite(food)
        }
    }


    fun removeMealFavorite(mealId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeMealFavorite(mealId)
        }
    }


    fun isMealInFavorite(foodId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isMealInFavorite(foodId)
    }
}
package com.example.applicationhome.ui.theme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.ItemScreenRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.CartUseCase
import com.example.applicationhome.domain.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemScreenViewModel @Inject constructor(
    private val favoriteRepository : FavoriteRepository,
    val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val itemScreenRepository : ItemScreenRepository,
    private val cartUseCase: CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase
) : ViewModel() {

    val userData = userRepository.userData



//       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    val selectedMeal = itemScreenRepository.selectedMeal
    val mealSize = itemScreenRepository.mealSize

    val selectedSnack = itemScreenRepository.selectedSnack
    val snackSize = itemScreenRepository.snackSize


    fun selectItem(item: FoodItem, size : String) {
        itemScreenRepository.selectMeal(item, size)
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    val cartInformation = cartRepository.cartInformation

    val totalPrice = MutableStateFlow(0.0)

    val errorInCart = MutableStateFlow(false)

    val newCount = MutableStateFlow(0)

    val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    val newFoodInCartSize = MutableStateFlow<String?>(null)


    fun updateCount(food : CartItemsClass, size : String, newCount : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.updateCount(userId, food, size, newCount)
            if(state != null){
                alertDialogTrue()
                newFoodInCartSize.value = state.first
                newFoodInCart.value = state.second
            }
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

    fun alertDialogTrue(){
        errorInCart.value = true
    }

    fun alertDialogFalse(){
        errorInCart.value = false
    }

    fun plusnewCount(){
        newCount.value += 1
    }

    fun minusnewCount(){
        newCount.value -= 1
    }


    //       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    val favoriteMealsIds = favoriteRepository.favoriteMealsIds


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
}
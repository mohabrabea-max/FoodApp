package com.example.applicationhome.features.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.ItemScreenRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.SnacksEntity
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


    val favoriteMealsIds = favoriteRepository.favoriteMealsIds

    val favoriteSnacksIds = favoriteRepository.favoriteSnacksIds

    val favoriteRestaurantsIds = favoriteRepository.favoriteRestaurantsIds


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

    val errorInCart = MutableStateFlow(false)


    val cartItems = cartRepository.cartItems


    val totalPrice = cartRepository.totalPrice

    val newCount = MutableStateFlow(0)

    val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    val newFoodInCartSize = MutableStateFlow<String?>(null)



    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
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

    fun deletenewCount(){
        newCount.value = 0
    }

    fun alertDialogTrue(){
        errorInCart.value = true
    }


    //       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    val typeInRestaurantScreen = MutableStateFlow("")
    val selectedTypeIndex = MutableStateFlow(0)

    fun selectedTypeInFavoriteScreen(
        index : Int,
        restaurantId : Int,
        navigation : () -> Unit
        ){
        viewModelScope.launch {
            val restaurant = favoriteRepository.getRestaurantToView(restaurantId)

            itemScreenRepository.selectRestaurant(restaurant)

            selectedTypeIndex.value = index
            typeInRestaurantScreen.value = restaurant.typ.toList().first()

            itemScreenRepository.selectedTypeInRestaurant(selectedTypeIndex.value, typeInRestaurantScreen.value)

            navigation()
        }
    }

    fun selectItem(item: MealsEntity, size : String) {
        itemScreenRepository.selectMeal(item, size)
    }

    fun selectSnack(item: SnacksEntity, size : String){
        itemScreenRepository.selectSnack(item, size)
    }
}
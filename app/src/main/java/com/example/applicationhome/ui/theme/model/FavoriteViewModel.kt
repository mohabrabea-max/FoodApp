package com.example.applicationhome.ui.theme.model



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.Snack
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.ItemScreenRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.CartUseCase
import com.example.applicationhome.domain.GetFavoriteUseCase
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
            userRepository.userData.collect { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    networkObserver.isNetworkAvailable.collect { available ->
                        isNetworkAvailable.value = available
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

            if(restaurant != null) itemScreenRepository.selectRestaurant(restaurant)

            selectedTypeIndex.value = index
            typeInRestaurantScreen.value = restaurant?.typ?.toList()?.first() ?: ""

            itemScreenRepository.selectedTypeInRestaurant(selectedTypeIndex.value, typeInRestaurantScreen.value)

            navigation()
        }
    }

    fun selectItem(item: FoodItem, size : String) {
        itemScreenRepository.selectMeal(item, size)
    }

    fun selectSnack(item: Snack, size : String){
        itemScreenRepository.selectSnack(item, size)
    }
}
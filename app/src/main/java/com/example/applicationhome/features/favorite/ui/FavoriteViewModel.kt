package com.example.applicationhome.features.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.data.model.AddToCartStates
import com.example.applicationhome.data.data.model.ShowSnackBarEvent
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModel @Inject constructor(
    cartRepository : CartRepository,
    private val userRepository : UserRepository,
    favoriteRepository : FavoriteRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase,
    networkObserver : NetworkObserver
) : ViewModel(){

    val userData = userRepository.userData


//        *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    private val _selectedCategorieInFavoriteScreen = MutableStateFlow(0)
    val selectedCategorieInFavoriteScreen = _selectedCategorieInFavoriteScreen.asStateFlow()

    val favoriteMeals = favoriteRepository.favoriteMeals

    val favoriteSnacks = favoriteRepository.favoriteSnacks

    val favoriteRestaurantsFromDatabase = favoriteRepository.favoriteRestaurantsFromDatabase

    val favoriteFoodCount = favoriteRepository.favoriteFoodCount

    val favoriteSnacksCount = favoriteRepository.favoriteSnacksCount

    val favoriteRestaurantsCount = favoriteRepository.favoriteRestaurantsCount

    val isNetworkAvailable = networkObserver.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )


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
        _selectedCategorieInFavoriteScreen.value = index
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    private val _errorInCart = MutableStateFlow<AddToCartStates>(AddToCartStates.Idle)
    val errorInCart = _errorInCart.asStateFlow()

    private val _snackBarChannel = Channel<ShowSnackBarEvent>(Channel.BUFFERED)
    val snackBarChannel = _snackBarChannel.receiveAsFlow()

    val cartItems = cartRepository.cartItems
    val cartInformation = cartRepository.cartInformation


    val totalPrice = cartRepository.totalPrice

    private val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    private val newFoodInCartSize = MutableStateFlow<String?>(null)



    fun plus(food: CartItemsClass, size : String, cartNavigation : () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)

            when(state){
                AddToCartStates.Success -> {
                    sendAddedToCartChannel{ cartNavigation() }
                }

                is AddToCartStates.ErrorInCartRestaurant -> {
                    alertDialogTrue(
                        AddToCartStates.ErrorInCartRestaurant(
                            title = "Start a new cart?",
                            message = "A new order will clear your cart with '${cartInformation.value?.restaurantName}'",
                            state.food,
                            state.size
                        )
                    )

                    newFoodInCartSize.value = state.size
                    newFoodInCart.value = state.food
                }

                is AddToCartStates.ErrorInLoginState -> {
                    alertDialogTrue(
                        AddToCartStates.ErrorInLoginState(
                            title = "Sign in required!",
                            message = "Please sign in or create an account to add items to your cart and proceed with your order."
                        )
                    )
                }

                else -> {}
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, food, size)
        }
    }

    fun clearAndStartNewCart(count : Int, cartNavigation : () -> Unit) {
        viewModelScope.launch {
            val newFood = newFoodInCart.value
            val newSize = newFoodInCartSize.value
            val userId = userRepository.userData.value.id
            val finally = cartUseCase.clearAndStartNewCart(userId, newFoodInCart.value, newFoodInCartSize.value)

            if(finally && newFood != null && newSize != null){
                cartUseCase.updateCount(userId, newFood, newSize, count)

                sendAddedToCartChannel{ cartNavigation() }

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

    private fun alertDialogTrue(error : AddToCartStates){
        _errorInCart.value = error
    }

    fun alertDialogFalse(){
        _errorInCart.value = AddToCartStates.Idle
    }

    private fun sendAddedToCartChannel(cartNavigation : () -> Unit){
        viewModelScope.launch {
            _snackBarChannel.send(
                ShowSnackBarEvent.AddedToCart(
                    message = "Added to cart",
                    actionLabel = "View",
                    action = { cartNavigation() }
                )
            )
        }
    }
}
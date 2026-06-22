package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.local.UserClass
import com.example.applicationhome.data.models.repository.CartRepository.cartItems
import com.example.applicationhome.data.models.repository.CartRepository.cartMeals
import com.example.applicationhome.data.models.repository.CartRepository.cartMealsMenu
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacks
import com.example.applicationhome.data.models.repository.CartRepository.cartSnacksMenu
import com.example.applicationhome.data.models.repository.CartRepository.getcart
import com.example.applicationhome.data.models.repository.CartRepository.totalNumber
import com.example.applicationhome.data.models.repository.CartRepository.totalPrice
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoritList
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoriteMeals
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoriteRestaurants
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoriteSnacks
import com.example.applicationhome.data.models.repository.FavoriteRepository.getFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.mealsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.restaurantsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.snacksFavorite
import com.example.applicationhome.data.models.repository.OrderRepository.getOrders
import com.example.applicationhome.data.models.repository.UserRepository
import com.example.applicationhome.data.models.repository.UserRepository.getActiveUserFromDatabase
import com.example.applicationhome.data.models.repository.UserRepository.getDataFromDatabase
import com.example.applicationhome.data.models.repository.UserRepository.isLogin
import com.example.applicationhome.data.models.repository.UserRepository.logOut
import com.example.applicationhome.data.models.repository.UserRepository.userData
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel(application : Application) : AndroidViewModel(application) {
    val emailstate = TextFieldState()
    val passwordstate = TextFieldState()
    var isEmailTrue by mutableStateOf(true)
    var isPasswordTrue by mutableStateOf(true)
    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)



    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }
        }
    }
    init {
        fetchUserDataFromDatabase()
    }


    fun bottonstate(){
        emailstate.clearText()
        passwordstate.clearText()
    }

    suspend fun getData(): String{
        val dataState = UserRepository.setUserDataToDatabase(emailstate.text.toString(), passwordstate.text.toString())
        when(dataState){
            "Password is true" -> {
                isEmailTrue = true
                isPasswordTrue = true
            }
            "Password is false" -> {
                isEmailTrue = true
                isPasswordTrue = false
            }
            "Email is false" -> {
                isEmailTrue = false
                isPasswordTrue = true
            }
            "Network error" -> {
               println("Error")
            }
        }
        return dataState
    }

    fun logout(){
        viewModelScope.launch {
            isLogin = false
            logOut()
            userData = UserClass("Guest")
            cartItems.clear()
            favoritList.clear()
            mealsFavorite.clear()
            snacksFavorite.clear()
            restaurantsFavorite.clear()
            totalPrice = 0.0
            totalNumber.value = 0
        }
    }

    fun login(){
        viewModelScope.launch {
            val result = getData()
            if(result == "Password is true"){
                userData = getDataFromDatabase()?: UserClass(firstname = "Guest")
                isLogin = true

                val favoriteDeferred = async { getFavorite() }
                val cartDeferred = async { getcart() }
                val ordersDeferred = async { getOrders() }

                favoriteDeferred.await()
                cartDeferred.await()
                ordersDeferred.await()

                val mealsDeferred = async { cartMeals() }
                val snacksDeferred = async { cartSnacks() }
                val favoriteMeals = async { favoriteMeals() }
                val favoriteSnacks = async { favoriteSnacks() }
                val favoriteRestaurants = async { favoriteRestaurants() }

                cartMealsMenu += mealsDeferred.await()
                cartSnacksMenu += snacksDeferred.await()
                mealsFavorite += favoriteMeals.await()
                snacksFavorite += favoriteSnacks.await()
                restaurantsFavorite += favoriteRestaurants.await()
            }
        }
    }

    fun fetchUserDataFromDatabase(){
        viewModelScope.launch {
            userData = getActiveUserFromDatabase()?: UserClass(firstname = "Guest")
            if(userData != UserClass(firstname = "Guest")){
                println(userData)
                isLogin = true
            }
        }
    }
}
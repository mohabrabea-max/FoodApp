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
import com.example.applicationhome.data.models.model.UserClass
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
import com.example.applicationhome.data.models.repository.FavoriteRepository.favoriteSnacks
import com.example.applicationhome.data.models.repository.FavoriteRepository.getFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.mealsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.restaurantsFavorite
import com.example.applicationhome.data.models.repository.FavoriteRepository.snacksFavorite
import com.example.applicationhome.data.models.repository.UserRepository
import com.example.applicationhome.data.models.repository.UserRepository.isLogin
import com.example.applicationhome.data.models.repository.UserRepository.userData
import com.example.applicationhome.data.models.repository.UserRepository.userId
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
                if(available){
                    getData()
                }
            }
        }
    }


    fun bottonstate(){
        emailstate.clearText()
        passwordstate.clearText()
    }

    fun getData(){
        viewModelScope.launch {
            val dataState = UserRepository.getUserData(emailstate.text.toString(), passwordstate.text.toString())
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
        }
    }

    fun logout(){
        isLogin = false
        userData = UserClass(
            "Guest",
            null,
            null,
            null,
            null,
            null
        )
        cartItems.clear()
        favoritList.clear()
        mealsFavorite = emptyList()
        snacksFavorite = emptyList()
        restaurantsFavorite = emptyList()
        totalPrice.value = 0.0
        totalNumber.value = 0
    }

    fun login(userdata : UserClass, userid : String){
        userData = userdata
        userId = userid
        isLogin = true
        viewModelScope.launch {
            getFavorite()
        }
        viewModelScope.launch {
            //addToMeals()
            getcart()
            cartMealsMenu += async { cartMeals() }.await()
            cartSnacksMenu += async { cartSnacks() }.await()
            mealsFavorite += async { favoriteMeals() }.await()
            snacksFavorite += async { favoriteSnacks() }.await()
        }

    }
}
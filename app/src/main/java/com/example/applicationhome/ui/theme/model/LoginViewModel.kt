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
import com.example.applicationhome.data.models.local.UpdateAccountState
import com.example.applicationhome.data.models.local.UserClass
import com.example.applicationhome.data.models.local.UsersDatabase
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

    private var userDao = UsersDatabase.getDaoInstance(application).userDao

    private var cartDao = UsersDatabase.getDaoInstance(application).cartDao


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
    init {
        fetchUserDataFromDatabase()
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

    suspend fun logout(){
        isLogin = false
        try {

            userDao.updateUser(UpdateAccountState(userData.email, isActive = false))
        }catch (e : Exception){
            println("Error")
        }
        userData = UserClass("Guest")
        cartItems.clear()
        favoritList.clear()
        mealsFavorite.clear()
        snacksFavorite.clear()
        restaurantsFavorite.clear()
        totalPrice = 0.0
        totalNumber.value = 0
    }

    suspend fun login(userid : String){
        try {
            userDao.addUser(userData)
            userDao.updateUser(UpdateAccountState(userData.email, isActive = true))
        }catch (e : Exception){
            println("Error")
        }

        userId = userid
        isLogin = true
        viewModelScope.launch {
            getFavorite()
        }
        viewModelScope.launch {
            //addToMeals()
            getcart()
            getOrders()
            val mealsDeferred = async { cartMeals() }
            val snacksDeferred = async { cartSnacks() }
            cartMealsMenu += mealsDeferred.await()
            cartSnacksMenu += snacksDeferred.await()
            val favoriteMeals = async { favoriteMeals() }
            val favoriteSnacks = async { favoriteSnacks() }
            val favoriteRestaurants = async { favoriteRestaurants() }
            mealsFavorite += favoriteMeals.await()
            snacksFavorite += favoriteSnacks.await()
            restaurantsFavorite += favoriteRestaurants.await()
        }
    }

    fun fetchUserDataFromDatabase(){
        viewModelScope.launch {
            try {
                val usersData = userDao.getAllUsers()
                println(usersData)
                val user = usersData.find { it.isActive == true }
                if(user != null){
                    isLogin = true
                    userData = user
                }
            } catch (e : Exception){
                println("Error")
            }
        }
    }
}
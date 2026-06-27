package com.example.applicationhome

import android.app.Application
import com.example.applicationhome.data.models.local.UsersDatabase
import com.example.applicationhome.data.models.repository.CartRepository
import com.example.applicationhome.data.models.repository.HomeScreenRepository
import com.example.applicationhome.data.models.repository.OrderRepository
import com.example.applicationhome.data.models.repository.RestaurantScreenRepository
import com.example.applicationhome.data.models.repository.UserRepository

class MyFoodApp : Application() {
    val database by lazy { UsersDatabase.getDaoInstance(this) }
    val cartdao by lazy { database.cartDao }
    val userdao by lazy { database.userDao }

    val homeScreenRepository by lazy { HomeScreenRepository() }

    val restaurantScreenRepository by lazy { RestaurantScreenRepository() }

    val userRepository by lazy { UserRepository(userdao) }

    val cartRepository by lazy { CartRepository(cartdao) }

    val orderRepository by lazy { OrderRepository() }
}
package com.example.applicationhome.data.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.applicationhome.data.data.local.dao.CartDao
import com.example.applicationhome.data.data.local.dao.FavoriteDao
import com.example.applicationhome.data.data.local.dao.OrdersDao
import com.example.applicationhome.data.data.local.dao.UsersDao
import com.example.applicationhome.data.data.local.entity.CartClass
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.data.local.entity.UserClass

@Database(
    entities = [
        UserClass::class,
        CartClass::class,
        CartItemsClass::class,
        FavoriteFoodDatabase::class,
        FavoriteSnacksDatabase::class,
        FavoriteRestaurantDatabase::class,
        OrdersDatabaseClass::class
    ],
    version = 35,
    exportSchema = false
)

@TypeConverters(FavoriteConverters::class)

abstract class UsersDatabase : RoomDatabase(){
    abstract val userDao : UsersDao
    abstract val cartDao : CartDao
    abstract val favoriteDao : FavoriteDao
    abstract val ordersDao : OrdersDao

}
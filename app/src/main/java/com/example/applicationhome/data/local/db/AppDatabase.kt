package com.example.applicationhome.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.applicationhome.data.local.dao.CartDao
import com.example.applicationhome.data.local.dao.FavoriteDao
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.dao.OrdersDao
import com.example.applicationhome.data.local.dao.UsersDao
import com.example.applicationhome.data.local.entity.CartClass
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.local.entity.RestaurantCategoryCrossRef
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SearchFtsEntity
import com.example.applicationhome.data.local.entity.SearchHistory
import com.example.applicationhome.data.local.entity.SnacksEntity
import com.example.applicationhome.data.local.entity.UserClass

@Database(
    entities = [
        UserClass::class,
        CartClass::class,
        CartItemsClass::class,
        MealsEntity::class,
        SnacksEntity::class,
        RestaurantsEntity::class,
        OrdersDatabaseClass::class,
        SearchFtsEntity::class,
        SearchHistory::class,
        FavoriteMealEntity::class,
        FavoriteSnackEntity::class,
        FavoriteRestaurantEntity::class,
        CategoriesEntity::class,
        OffersEntity::class,
        RestaurantCategoryCrossRef::class
    ],
    version = 60,
    exportSchema = false
)

@TypeConverters(FavoriteConverters::class)

abstract class UsersDatabase : RoomDatabase(){
    abstract val userDao : UsersDao
    abstract val cartDao : CartDao
    abstract val favoriteDao : FavoriteDao
    abstract val ordersDao : OrdersDao
    abstract val foodAndRestaurantsDao : FoodAndRestaurantsDao

}
package com.example.applicationhome.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.applicationhome.core.data.local.dao.CartDao
import com.example.applicationhome.core.data.local.dao.FavoriteDao
import com.example.applicationhome.core.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.core.data.local.dao.OrdersDao
import com.example.applicationhome.core.data.local.dao.ReviewsDao
import com.example.applicationhome.core.data.local.dao.UsersDao
import com.example.applicationhome.core.data.local.entity.AddressesEntity
import com.example.applicationhome.core.data.local.entity.CartClass
import com.example.applicationhome.core.data.local.entity.CartItemsClassEntity
import com.example.applicationhome.core.data.local.entity.CategoriesEntity
import com.example.applicationhome.core.data.local.entity.DiscountsEntity
import com.example.applicationhome.core.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.core.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.core.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.core.data.local.entity.LastUpdateReviewsEntity
import com.example.applicationhome.core.data.local.entity.MealsEntity
import com.example.applicationhome.core.data.local.entity.OffersEntity
import com.example.applicationhome.core.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.core.data.local.entity.RestaurantCategoryCrossRef
import com.example.applicationhome.core.data.local.entity.RestaurantsEntity
import com.example.applicationhome.core.data.local.entity.ReviewsEntity
import com.example.applicationhome.core.data.local.entity.ReviewsStarsEntity
import com.example.applicationhome.core.data.local.entity.SearchFtsEntity
import com.example.applicationhome.core.data.local.entity.SearchHistory
import com.example.applicationhome.core.data.local.entity.SnacksEntity
import com.example.applicationhome.core.data.local.entity.UserClass

@Database(
    entities = [
        UserClass::class,
        CartClass::class,
        CartItemsClassEntity::class,
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
        RestaurantCategoryCrossRef::class,
        AddressesEntity::class,
        DiscountsEntity::class,
        ReviewsEntity::class,
        LastUpdateReviewsEntity::class,
        ReviewsStarsEntity::class,
    ],
    version = 68,
    exportSchema = false
)

@TypeConverters(DataConverters::class)

abstract class UsersDatabase : RoomDatabase(){
    abstract val userDao : UsersDao
    abstract val cartDao : CartDao
    abstract val favoriteDao : FavoriteDao
    abstract val ordersDao : OrdersDao
    abstract val foodAndRestaurantsDao : FoodAndRestaurantsDao
    abstract val reviewsDao : ReviewsDao
}
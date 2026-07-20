package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.applicationhome.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.local.entity.FavoriteSnacksDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    //             --------------------------------------   Meals    -------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFoodToFavorite(foodItem : List<FavoriteFoodDatabase>)

    @Query("DELETE FROM favorite_food WHERE userId = :userId AND mealId IN (:mealIds)")
    suspend fun deleteFoodFromDatabase(userId: String, mealIds: List<Int>)

    @Query("SELECT * FROM favorite_food WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getFoodDeletedOffline() : List<FavoriteFoodDatabase>

    @Query("UPDATE favorite_food SET isDeletedOffline = 1 WHERE userId = :userId AND mealId = :mealId")
    suspend fun markFoodAsDeletedOffline(userId: String, mealId: Int)

    @Query("SELECT * FROM favorite_food WHERE userId = :userId AND isDeletedOffline = 0")
    fun getFoodFromDatabase(userId : String) : Flow<List<FavoriteFoodDatabase>>

    @Query("SELECT * FROM favorite_food WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedFood() : List<FavoriteFoodDatabase>

    @Update
    suspend fun markMealsAsSynced(meals: List<FavoriteFoodDatabase>)

    @Query("DELETE FROM favorite_food WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedMeals()

    //             --------------------------------------   Snacks    -------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSnacksToFavorite(snacksItems : List<FavoriteSnacksDatabase>)

    @Query("DELETE FROM favorite_snacks WHERE userId = :userId AND snackId IN (:snackIds)")
    suspend fun deleteSnacksFromDatabase(userId: String, snackIds: List<Int>)

    @Query("SELECT * FROM favorite_snacks WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getSnacksDeletedOffline() : List<FavoriteSnacksDatabase>

    @Query("UPDATE favorite_snacks SET isDeletedOffline = 1 WHERE userId = :userId AND snackId = :snackId")
    suspend fun markSnacksAsDeletedOffline(userId: String, snackId: Int)

    @Query("SELECT * FROM favorite_snacks WHERE userId = :userId AND isDeletedOffline = 0")
    fun getSnacksFromDatabase(userId : String) : Flow<List<FavoriteSnacksDatabase>>

    @Query("SELECT * FROM favorite_snacks WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedSnacks() : List<FavoriteSnacksDatabase>

    @Update
    suspend fun markSnacksAsSynced(snacks: List<FavoriteSnacksDatabase>)

    @Query("DELETE FROM favorite_snacks WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedSnacks()

    //              --------------------------------------   Restaurants    -------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRestaurantToFavorite(restaurant : List<FavoriteRestaurantDatabase>)

    @Query("DELETE FROM favorite_restaurant WHERE userId = :userId AND restaurantId IN (:resIds)")
    suspend fun deleteRestaurantFromDatabase(userId: String, resIds: List<Int>)

    @Query("SELECT * FROM favorite_restaurant WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getRestaurantsDeletedOffline() : List<FavoriteRestaurantDatabase>

    @Query("UPDATE favorite_restaurant SET isDeletedOffline = 1 WHERE userId = :userId AND restaurantId = :resId")
    suspend fun markRestaurantsAsDeletedOffline(userId: String, resId: Int)

    @Query("SELECT * FROM favorite_restaurant WHERE userId = :userId AND isDeletedOffline = 0")
    fun getRestaurantsFromDatabase(userId : String) : Flow<List<FavoriteRestaurantDatabase>>

    @Query("SELECT * FROM favorite_restaurant WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedRestaurants() : List<FavoriteRestaurantDatabase>

    @Update
    suspend fun markRestaurantsAsSynced(restaurants: List<FavoriteRestaurantDatabase>)

    @Query("DELETE FROM favorite_restaurant WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedRestaurants()
}
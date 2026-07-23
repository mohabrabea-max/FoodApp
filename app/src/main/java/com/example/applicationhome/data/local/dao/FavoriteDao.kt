package com.example.applicationhome.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    //             --------------------------------------   Meals    -------------------------------------

    @Transaction
    @Query("""
        SELECT * FROM meals_entity
        WHERE id IN(
        SELECT mealId FROM favorite_meals
        WHERE userId =:userId AND isDeletedOffline = 0
        )
            """)
    fun getFoodFromDatabase(userId : String) : Flow<List<MealWithFavoriteStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFoodToFavorite(foodItem : List<FavoriteMealEntity>)

    @Query("DELETE FROM favorite_meals WHERE userId = :userId AND mealId IN (:mealIds)")
    suspend fun deleteFoodFromDatabase(userId: String, mealIds: List<Int>)

    @Query("SELECT * FROM favorite_meals WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getFoodDeletedOffline() : List<FavoriteMealEntity>

    @Query("UPDATE favorite_meals SET isDeletedOffline = 1 WHERE userId = :userId AND mealId = :mealId")
    suspend fun markFoodAsDeletedOffline(userId: String, mealId: Int)

    @Query("SELECT * FROM favorite_meals WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedFood() : List<FavoriteMealEntity>

    @Update
    suspend fun markMealsAsSynced(meals: List<FavoriteMealEntity>)

    @Query("DELETE FROM favorite_meals WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedMeals()


    //             --------------------------------------   Snacks    -------------------------------------

    @Transaction
    @Query("""
        SELECT * FROM snacks_entity
        WHERE id IN(
        SELECT snackId FROM favorite_snacks
        WHERE userId =:userId AND isDeletedOffline = 0
        )
            """)
    fun getSnacksFromDatabase(userId : String) : Flow<List<SnackWithFavoriteStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSnacksToFavorite(snacksItems : List<FavoriteSnackEntity>)

    @Query("DELETE FROM favorite_snacks WHERE userId = :userId AND snackId IN (:snackIds)")
    suspend fun deleteSnacksFromDatabase(userId: String, snackIds: List<Int>)

    @Query("SELECT * FROM favorite_snacks WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getSnacksDeletedOffline() : List<FavoriteSnackEntity>

    @Query("UPDATE favorite_snacks SET isDeletedOffline = 1 WHERE userId = :userId AND snackId = :snackId")
    suspend fun markSnacksAsDeletedOffline(userId: String, snackId: Int)

    @Query("SELECT * FROM favorite_snacks WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedSnacks() : List<FavoriteSnackEntity>

    @Update
    suspend fun markSnacksAsSynced(snacks: List<FavoriteSnackEntity>)

    @Query("DELETE FROM favorite_snacks WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedSnacks()


    //              --------------------------------------   Restaurants    -------------------------------------

    @Transaction
    @Query("""
        SELECT * FROM restaurants_entity
        WHERE id IN(
        SELECT resId FROM favorite_restaurants
        WHERE userId =:userId AND isDeletedOffline = 0
        )
            """)
    fun getRestaurantsFromDatabase(userId : String) : Flow<List<RestaurantWithFavoriteStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRestaurantToFavorite(restaurant : List<FavoriteRestaurantEntity>)

    @Query("DELETE FROM favorite_restaurants WHERE userId = :userId AND resId IN (:resIds)")
    suspend fun deleteRestaurantFromDatabase(userId: String, resIds: List<Int>)

    @Query("SELECT * FROM favorite_restaurants WHERE isDeletedOffline = 1 AND isSynced = 1")
    suspend fun getRestaurantsDeletedOffline() : List<FavoriteRestaurantEntity>

    @Query("UPDATE favorite_restaurants SET isDeletedOffline = 1 WHERE userId = :userId AND resId = :resId")
    suspend fun markRestaurantsAsDeletedOffline(userId: String, resId: Int)

    @Query("SELECT * FROM favorite_restaurants WHERE isSynced = 0 AND isDeletedOffline = 0")
    suspend fun getUnSyncedRestaurants() : List<FavoriteRestaurantEntity>

    @Update
    suspend fun markRestaurantsAsSynced(restaurants: List<FavoriteRestaurantEntity>)

    @Query("DELETE FROM favorite_restaurants WHERE isDeletedOffline = 1 AND isSynced = 0")
    suspend fun cleanUpLocalOnlyDeletedRestaurants()
}
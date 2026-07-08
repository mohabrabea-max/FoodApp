package com.example.applicationhome.data.models.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.applicationhome.data.models.local.entity.CartClass
import com.example.applicationhome.data.models.local.entity.CartItemsClass
import com.example.applicationhome.data.models.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.models.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.models.local.entity.UpdateAccountState
import com.example.applicationhome.data.models.local.entity.UserClass
import kotlinx.coroutines.flow.Flow


//                       *** -------------------------------------- \\***  User Data  ***// ------------------------------------- ***
@Dao
interface UsersDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز
//    @Query("SELECT * FROM users")
//    suspend fun getAllUsers(): List<UserClass>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getOneUser(userId : String): Flow<UserClass?>

    @Query("SELECT * FROM users WHERE isActive = :isActive")
    fun getActiveUser(isActive : Boolean): Flow<UserClass?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)   // IGNORE دي بتتجاهل اي داتا عايز اضيفها فيها ايميل مطابق لايميل موجود قبل كدا
                                                       // REPLACE  بتستبدل الداتا القديمة بالجديدة لو الايميل متكرر في الداتا بيز
    suspend fun addUser(user : UserClass)

    @Update(entity = UserClass::class)
    suspend fun updateUser(updateState: UpdateAccountState)
}


//                       *** -------------------------------------- \\***  Cart  ***// ------------------------------------- ***
@Dao
interface CartDao {            // دا الجزء اللي بينفذ عمليات في الداتا بيز

    //               --------------------------------------   Cart Items    -------------------------------------

    @Query("SELECT * FROM cart_items WHERE userId = :userid")
    fun getCartItems(userid : String): Flow<List<CartItemsClass?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCartItem(cartItem : CartItemsClass)

    @Query("UPDATE cart_items SET quantity = :newQuantity, totalPrice = :newTotalPrice WHERE userId = :userId AND mealKey = :mealkey")
    suspend fun updateCartItem(newQuantity: Int, newTotalPrice : Double, userId : String, mealkey : String)

    @Query("DELETE FROM cart_items WHERE userId = :userId AND mealKey = :mealkey")
    suspend fun deleteItemFromCart(mealkey : String, userId : String)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun deleteAllItemFromCart(userId : String)


    //             --------------------------------------   Parent Cart    -------------------------------------


    @Query("SELECT * FROM cart WHERE userId = :userid")
    fun getParentCart(userid : String): Flow<CartClass?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createParentCart(cart : CartClass)

    @Query("DELETE FROM cart WHERE userId = :userid")
    suspend fun deleteParentCart(userid : String)
}


//                       *** -------------------------------------- \\***  Orders History  ***// ------------------------------------- ***
@Dao
interface OrdersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewOrders(orders : List<OrdersDatabaseClass>)

    @Query("SELECT * FROM orders_history WHERE userId =:userId ORDER BY orderId DESC")
    fun getAllOrders(userId : String) : Flow<List<OrdersDatabaseClass>>
}


//                         *** -------------------------------------- \\***  Favorite  ***// ------------------------------------- ***
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
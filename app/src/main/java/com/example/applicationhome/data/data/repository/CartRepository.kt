package com.example.applicationhome.data.data.repository

import com.example.applicationhome.data.data.local.dao.CartDao
import com.example.applicationhome.data.data.local.entity.CartClass
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.remote.FoodAppAPIs
import com.example.applicationhome.domain.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class CartRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val cartdao: CartDao,
    private val api : FoodAppAPIs,
    @ApplicationScope private val externalScope: CoroutineScope
) {
    val cartInformation: StateFlow<CartClass?> =
        userRepository.userData
            .flatMapLatest { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    getCartData(id)
                }else {
                    flowOf(null)
                }
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    val cartItems: StateFlow<List<CartItemsClass?>> =
        userRepository.userData
            .flatMapLatest { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    getCartItems(id)
                } else {
                    flowOf(emptyList())
                }
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val totalNumber: StateFlow<Int> =
        cartItems
            .map { item -> item.sumOf { it?.quantity ?: 0 } }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )


    fun getCartItems(id : String): Flow<List<CartItemsClass?>> = cartdao.getCartItems(id)

    fun getCartData(id : String) : Flow<CartClass?> = cartdao.getParentCart(id)

    suspend fun getCartRestaurantData(food : CartItemsClass) : Restaurants?{
        return try {
            val response = api.getCarRestaurant("\"id\"", food.restaurantId)
            val resData = response.body()?.values?.first()
            if(response.isSuccessful && resData != null){
                resData
            }else{
                null
            }
        } catch (e : Exception){
            null
        }
    }

    suspend fun createNewCart(userId : String, food: CartItemsClass, size : String, type : String, priceOfOne : Double, res : Restaurants, number: Int) : String{
        val mealKey = "${food.mealId}_$size"
        val cartObject = CartClass(userId, res.id, res.name, res.image)
        return try {
            val cartItemsObject = CartItemsClass(
                userId,
                mealKey,
                food.mealId,
                food.name,
                type,
                size,
                number,
                priceOfOne,
                priceOfOne * number,
                food.image,
                food.restaurantId
            )
            cartdao.createParentCart(cartObject)
            cartdao.addCartItem(cartItemsObject)
            "Success"
        } catch (e : Exception){
            "Error"
        } finally {
            ""
        }
    }

    suspend fun addMealToCart(userId : String, food: CartItemsClass, size : String, type : String, priceOfOne : Double, number: Int): String{
        val mealKey = "${food.mealId}_$size"
        val cartItemsObject = CartItemsClass(
            userId,
            mealKey,
            food.mealId,
            food.name,
            type,
            size,
            number,
            priceOfOne,
            priceOfOne * number,
            food.image,
            food.restaurantId
        )
        return try {
            cartdao.addCartItem(cartItemsObject)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    suspend fun updateQuantity(userId : String, food: CartItemsClass, size : String, priceOfOne : Double, number: Int): String{
        val mealKey = "${food.mealId}_${size}"
        return try {
            cartdao.updateCartItem(
                number,
                priceOfOne * number,
                userId,
                mealKey
            )
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    suspend fun deleteFromCart(userId : String, foodId: Int, size : String): String{
        val mealKey = "${foodId}_${size}"
        return try {
            cartdao.deleteItemFromCart(mealKey, userId)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    suspend fun deleteParentCart(userId : String): String{
        return try {
            cartdao.deleteParentCart(userId)
            "Success"
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    suspend fun deleteAllCart(userId : String): String{
        return try {
            cartdao.deleteAllItemFromCart(userId)
            "Success"
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

//    suspend fun getMeal(mealId : Int): FoodItem? {
//        val mealKey = "Meal_${mealId}"
//        if(foodMenuList[mealKey] != null){
//            return foodMenuList[mealKey]
//        }else{
//            return try {
//                val response = RetrofitInstance.api.getCartMeal(mealKey)
//                if(response.isSuccessful){
//                    return response.body()?.values?.firstOrNull()
//                }else{
//                    null
//                }
//            }catch (e : Exception){
//                null
//            }
//        }
//    }
}
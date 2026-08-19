package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.local.dao.CartDao
import com.example.applicationhome.data.local.entity.CartClass
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.remote.FoodAppAPIs
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

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImpl @Inject constructor(
    userRepository : UserRepository,
    private val cartdao : CartDao,
    private val api : FoodAppAPIs,
    @ApplicationScope private val externalScope: CoroutineScope
): CartRepository {
    override val cartInformation: StateFlow<CartClass?> =
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

    override val cartItems: StateFlow<List<CartItemsClass>> =
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

    override val totalNumber: StateFlow<Int> =
        cartItems
            .map { item -> item.sumOf { it.quantity ?: 0 } }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    override val totalPrice : StateFlow<Double> =
        cartItems.map { cartList ->
            cartList.sumOf { it.totalPrice }
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )


    override fun getCartItems(id : String): Flow<List<CartItemsClass>> = cartdao.getCartItems(id)

    override fun getCartData(id : String) : Flow<CartClass?> = cartdao.getParentCart(id)

    override suspend fun getCartRestaurantData(food : CartItemsClass) : Restaurants{
        return try {
            val response = api.getCarRestaurant("\"id\"", food.restaurantId)
            val resData = response.body()?.values?.first()
            if(response.isSuccessful && resData != null){
                resData
            }else{
                Restaurants()
            }
        } catch (e : Exception){
            Restaurants()
        }
    }

    override suspend fun createNewCart(
        userId : String,
        food: CartItemsClass,
        size : String,
        type : String,
        priceOfOne : Double,
        res : Restaurants,
        number: Int
    ) : String{

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

    override suspend fun addMealToCart(
        userId : String,
        food: CartItemsClass,
        size : String,
        type : String,
        priceOfOne : Double,
        number: Int
    ): String{

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

    override suspend fun updateQuantity(
        userId : String,
        food: CartItemsClass,
        size : String,
        priceOfOne : Double,
        number: Int
    ): String{
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

    override suspend fun deleteFromCart(userId : String, foodId: Int, size : String): String{
        val mealKey = "${foodId}_${size}"
        return try {
            cartdao.deleteItemFromCart(mealKey, userId)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    override suspend fun deleteParentCart(userId : String): String{
        return try {
            cartdao.deleteParentCart(userId)
            "Success"
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    override suspend fun deleteAllCart(userId : String): String{
        return try {
            cartdao.deleteAllItemFromCart(userId)
            "Success"
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }
}
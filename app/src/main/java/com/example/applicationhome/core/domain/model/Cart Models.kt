package com.example.applicationhome.core.domain.model

data class CartItemsDomainClass(
    val userId : String = "",
    val mealKey : String = "",
    val mealId : Int = 0,
    val name : String = "",
    val type : String = "",
    val size : String = "",
    val quantity: Int = 0,
    val priceOfOne : Double = 0.0,
    val totalPrice : Double = 0.0,
    val image : String = "",
    val restaurantId : Int = 0,
    val discount : DiscountsDomainClass? = null
){
    private val dis = discount?.discount
    val finalPriceOfOne = if(dis != null){
            priceOfOne * (1.0 - (dis.toDouble() / 100.00))
        }else{
            priceOfOne
        }
}
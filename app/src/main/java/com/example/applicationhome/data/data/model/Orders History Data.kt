package com.example.applicationhome.data.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.applicationhome.R

enum class OrderStatesEnum(val rawValue : String){
    PREPARING("PREPARING"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");

    companion object {
        fun fromString(value : String?): OrderStatesEnum {
            return entries.find { it.rawValue.equals(value, ignoreCase = true) } ?: PREPARING
        }
    }
}

sealed class OrderStates(val index : Int, @StringRes val title : Int, val enumState : OrderStatesEnum){
    data object Preparing : OrderStates(0, R.string.tab_preparing, OrderStatesEnum.PREPARING)
    data object Delivering : OrderStates(1, R.string.tab_delivering, OrderStatesEnum.DELIVERING)
    data object Delivered : OrderStates(2, R.string.tab_delivered, OrderStatesEnum.DELIVERED)
    data object Cancelled : OrderStates(3, R.string.tab_cancelled, OrderStatesEnum.CANCELLED)

    companion object {
        fun fromEnum(enumState: OrderStatesEnum): OrderStates {
            return when(enumState){
                OrderStatesEnum.PREPARING -> Preparing
                OrderStatesEnum.DELIVERING -> Delivering
                OrderStatesEnum.DELIVERED -> Delivered
                OrderStatesEnum.CANCELLED -> Cancelled
            }
        }
    }
}

sealed class OrdersHistoryScreens(val index : Int, @StringRes val title : Int, val enumState : OrderStatesEnum){
    data object Preparing : OrdersHistoryScreens(0, R.string.tab_preparing, OrderStatesEnum.PREPARING)
    data object Delivered : OrdersHistoryScreens(1, R.string.tab_delivered, OrderStatesEnum.DELIVERED)
    data object Cancelled : OrdersHistoryScreens(2, R.string.tab_cancelled, OrderStatesEnum.CANCELLED)
}

data class OrderUiClass(
    val orderId : Long = 0,
    val userId : String = "",
    val date : String = "",
    val state : OrderStates = OrderStates.Preparing,
    val subtotal : Double = 0.0,
    val delivery : Double = 0.0,
    val service : Double = 0.0,
    val totalPrice : Double = 0.0,
    val restaurantName : String = "",
    val restaurantImage : String = "",
    val restaurantId : Int = 0,
    val userInformation : UserInformationInOrderClass = UserInformationInOrderClass(),
    val orderItems : List<OrderItemsClass> = emptyList(),
    val orderHistory : List<OrderHistoryClass> = emptyList()
)

data class TimelineStep(
    val enumState : OrderStatesEnum,
    val subtitle : String,
    val date : String,
    val icon : ImageVector,
    val isCompleted : Boolean,
    val isCurrent : Boolean
)
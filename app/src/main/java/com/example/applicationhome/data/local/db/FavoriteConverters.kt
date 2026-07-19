package com.example.applicationhome.data.local.db

import androidx.room.TypeConverter
import com.example.applicationhome.data.data.model.MealSizeDetail
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.UserInformationInOrderClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteConverters{
    private val gson = Gson()


    @TypeConverter
    fun fromStringList(typeList : List<String>): String?{
        return gson.toJson(typeList)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        if(json == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }


    @TypeConverter
    fun fromMealSizeDetailList(mealSizeOptions : List<MealSizeDetail>): String? {
        return gson.toJson(mealSizeOptions)
    }
    @TypeConverter
    fun toMealSizeDetailList(json : String?): List<MealSizeDetail>? {
        if(json == null) return null
        val type = object : TypeToken<List<MealSizeDetail>>() {}.type
        return gson.fromJson(json, type)
    }


    @TypeConverter
    fun fromSnackSizeDetailList(snackpriceANDsize : Map<String, Double>): String? {
        return gson.toJson(snackpriceANDsize)
    }
    @TypeConverter
    fun toSnackSizeDetailList(json : String?): Map<String, Double>? {
        if(json == null) return null
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(json, type)
    }


    @TypeConverter
    fun fromUserInformationInOrderClass(userInformation: UserInformationInOrderClass): String? {
        return gson.toJson(userInformation)
    }
    @TypeConverter
    fun toUserInformationInOrderClass(json: String?): UserInformationInOrderClass? {
        if(json == null) return null
        val type = object : TypeToken<UserInformationInOrderClass>() {}.type
        return gson.fromJson(json, type)
    }


    @TypeConverter
    fun fromOrderItemsClass(orderItemsClass : List<OrderItemsClass>): String? {
        return gson.toJson(orderItemsClass)
    }
    @TypeConverter
    fun toOrderItemsClass(json : String?): List<OrderItemsClass>? {
        if(json == null) return null
        val type = object : TypeToken<List<OrderItemsClass>>() {}.type
        return gson.fromJson(json, type)
    }
}
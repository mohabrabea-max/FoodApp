package com.example.applicationhome.data.models.local.db

import androidx.room.TypeConverter
import com.example.applicationhome.data.models.model.MealSizeDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteConverters{
    private val gson = Gson()

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
}
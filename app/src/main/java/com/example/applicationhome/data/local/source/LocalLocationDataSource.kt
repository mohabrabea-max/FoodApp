package com.example.applicationhome.data.local.source

import android.content.Context
import com.example.applicationhome.data.data.model.City
import com.example.applicationhome.data.data.model.Governorate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * قراءة ملف egypt.json من الـ Assets وتحويله إلى قائمة من المحافظات والمدن.
 */

@Singleton
class LocalLocationDataSource @Inject constructor(
    @ApplicationContext private val context: Context
){
    fun getLocationsFromJson(): List<Governorate> {
        val jsonString = context.assets.open("egypt.json").bufferedReader().use{ it.readText() }  // قرأنا ملف الJSON

        val type = object : TypeToken<Map<String, Map<String, Map<String, String>>>>() {}.type // حددنا شكل الداتا في ملف الJSON

        val countryMap : Map<String, Map<String, Map<String, String>>> = Gson().fromJson(jsonString, type) // عملنا تحويل من JSON للشكل اللي حددناه السطر اللي فات

        val egyptData = countryMap["Egypt"] ?: return emptyList() // حددنا الدولة اللي عايزينها من الماب اللي طلعتلنا

        return egyptData.map { (govName, citiesMap) ->         // عملنا ريتيرن لليست من نوع Governorate
            val citiesList = citiesMap
                .map { (cityEnglishName, cityArabicName) ->
                    City(
                        englishName = cityEnglishName,
                        arabicName = cityArabicName
                    )
                }
            Governorate(
                name = govName,
                cities = citiesList
            )
        }
    }
}
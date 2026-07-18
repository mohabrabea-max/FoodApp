package com.example.applicationhome.data.data.local.source

import android.content.Context
import com.example.applicationhome.data.data.model.City
import com.example.applicationhome.data.data.model.Governorate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalLocationDataSource @Inject constructor(
    @ApplicationContext private val context: Context
){
    fun getLocationsFromJson(): List<Governorate> {
        val jsonString = context.assets.open("egypt.json").bufferedReader().use{ it.readText() }

        val type = object : TypeToken<Map<String, Map<String, Map<String, String>>>>() {}.type

        val countryMap : Map<String, Map<String, Map<String, String>>> = Gson().fromJson(jsonString, type)

        val egyptData = countryMap["Egypt"] ?: return emptyList()

        return egyptData.map { (govName, citiesMap) ->
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
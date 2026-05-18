package com.example.applicationhome.view.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.Categories
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Offers
import com.example.applicationhome.data.models.Restaurants
import com.example.applicationhome.data.models.RetrofitInstance
import com.example.applicationhome.data.models.Snack
import kotlinx.coroutines.launch

class APIData : ViewModel(){
    var foodMenuList by mutableStateOf<List<FoodItem>>(emptyList())
        private set
    var foodMenuListisLoading by mutableStateOf(true)


    var restaurantsMenu by mutableStateOf<List<Restaurants>>(emptyList())
        private set
    var restaurantsMenuisLoading by mutableStateOf(true)


    var categories by mutableStateOf<List<Categories>>(emptyList())
        private set
    var categoriesisLoading by mutableStateOf(true)


    var snacks by mutableStateOf<List<Snack>>(emptyList())
        private set
    var snacksisLoading by mutableStateOf(true)


    var offers by mutableStateOf<List<Offers>>(emptyList())
        private set
    var offersisLoading by mutableStateOf(true)



    init {
        // أول ما الـ ViewModel يفتح، بنروح نجيب الداتا الحقيقية أونلاين
        loadDataFromApi()
    }
    private fun loadDataFromApi() {
        viewModelScope.launch {
            try {
                foodMenuListisLoading = true
                // بنجيب الأكل والمطاعم باستخدام الـ RetrofitInstance المطور بتاعنا
                foodMenuList = RetrofitInstance.api.foodmenu()
            } catch (e: Exception) {
                // معالجة الخطأ لو النت قطع
                e.printStackTrace()
            } finally {
                foodMenuListisLoading = false
            }
        }

        viewModelScope.launch {
            try {
                restaurantsMenuisLoading = true
                restaurantsMenu = RetrofitInstance.api.restaurants()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                restaurantsMenuisLoading = false
            }
        }

        viewModelScope.launch {
            try {
                categoriesisLoading = true
                categories = RetrofitInstance.api.categorieslist()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                categoriesisLoading = false
            }
        }

        viewModelScope.launch {
            try {
                snacksisLoading = true
                snacks = RetrofitInstance.api.snakesMenu()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                snacksisLoading = false
            }
        }

        viewModelScope.launch {
            try {
                offersisLoading = true
                offers = RetrofitInstance.api.offers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                offersisLoading = false
            }
        }
    }
}
package com.example.applicationhome.view.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class APIData(application : Application) : AndroidViewModel(application){
    var foodMenuList by mutableStateOf<List<FoodItem>>(emptyList()); private set
    var foodMenuListisLoading by mutableStateOf(true)


    var restaurantsMenu by mutableStateOf<List<Restaurants>>(emptyList()); private set
    var restaurantsMenuisLoading by mutableStateOf(true)


    var categories by mutableStateOf<List<Categories>>(emptyList()); private set
    var categoriesisLoading by mutableStateOf(true)


    var snacks by mutableStateOf<List<Snack>>(emptyList()); private set
    var snacksisLoading by mutableStateOf(true)


    var offers by mutableStateOf<List<Offers>>(emptyList()); private set
    var offersisLoading by mutableStateOf(true)


    var isNetworkAvailable by mutableStateOf(true)   //   دي عشان نظهر رسالة تحذير لما النت يفصل


    init {
        observeNetworkChanges() //  أول ما الـ ViewModel يبدأ، بنشغل مراقب النت فوراً
    }

    private fun observeNetworkChanges(){
        val connectivityManager = getApplication<Application>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isNetworkAvailable = true
                if(foodMenuList.isEmpty()){
                    loadDataFromApi()
                }
            }
            override fun onLost(network: Network){
                super.onLost(network)
                isNetworkAvailable = false
            }
        })
    }

    fun loadDataFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                restaurantsMenuisLoading = true
                restaurantsMenu = RetrofitInstance.api.restaurants()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                restaurantsMenuisLoading = false
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                categoriesisLoading = true
                categories = RetrofitInstance.api.categorieslist()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                categoriesisLoading = false
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                snacksisLoading = true
                snacks = RetrofitInstance.api.snacksMenu()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                snacksisLoading = false
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
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
package com.example.applicationhome.data.models

sealed class Screens (val screen : String){
    data object ItemScreen : Screens("itemScreen")
    data object HomeScreen : Screens("homescreen")
    data object Profile : Screens("profile")
    data object Settings : Screens("settings")
    data object Search : Screens("search")
    data object Menu : Screens("menu")
    data object Restaurants : Screens("restaurants")
    data object Varieties : Screens("varieties")
}
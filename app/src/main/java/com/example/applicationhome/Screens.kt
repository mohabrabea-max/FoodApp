package com.example.applicationhome

sealed class Screens (val screen : String){
    data object HomeScreen : Screens("homescreen")
    data object Profile : Screens("profile")
    data object Settings : Screens("settings")
    data object Menu : Screens("menu")
    data object Restaurants : Screens("restaurants")
    data object Varieties : Screens("varieties")
}
package com.example.applicationhome.data.models

sealed class Screens (val screen : String){
    data object ItemScreen : Screens("itemScreen")
    data object HomeScreen : Screens("homescreen")
    data object Profile : Screens("profile")
    data object Settings : Screens("settings")
    data object Notifications : Screens("notifications")
    data object Search : Screens("search")
    data object Menu : Screens("menu")
    data object Restaurants : Screens("restaurants")
    data object Cart : Screens("cart")
    data object Favorite : Screens("favorite")
    data object LoginScreen : Screens("login")
    data object SignUpScreen : Screens("signup")
}
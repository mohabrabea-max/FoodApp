package com.example.applicationhome

sealed class Screens (val screen : String){
    data object HomeScreen : Screens("homescreen")
    data object Menu : Screens("menu")
}
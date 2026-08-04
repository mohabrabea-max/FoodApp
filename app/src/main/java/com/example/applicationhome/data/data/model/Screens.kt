package com.example.applicationhome.data.data.model

sealed class Screens (val screen : String){
    //data object ItemScreen : Screens("itemScreen")
    data object HomeScreen : Screens("homescreen")
    data object Profile : Screens("profile")
    data object Settings : Screens("settings")
    data object Notifications : Screens("notifications")
    data object Search : Screens("search")
    data object RestaurantScreen : Screens("restaurantscreen/{restaurantId}?mealId={mealId}") {
        fun createRoute(restaurantId: Int, mealId: Int? = null): String {
            return if(mealId != null){
                "restaurantscreen/$restaurantId?mealId=$mealId"
            }else{
                "restaurantscreen/$restaurantId"
            }
        }
    }
    data object Cart : Screens("cart")
    data object Favorite : Screens("favorite")
    data object LoginScreen : Screens("login")
    data object SignUpScreen : Screens("signup")
    data object ConfirmOrderScreen : Screens("confirmorderscreen")
    data object LastOrdersScreen : Screens("lastordersscreen")
    data object OrderScreen : Screens("orderscreen")
    data object NoInternetScreen : Screens("nointernetscreen")
    data object DashboardScreen : Screens("dashboardscreen")
}
package com.suma.sumaapp.navigation

sealed class Destinations(val route: String) {
    object launcchB : Destinations("launcchB")
    /*Для экранов с параметрами:
    object Details : Destinations("details/{itemId}") {
        fun createRoute(itemId: String) = "details/$itemId"
    }*/
}
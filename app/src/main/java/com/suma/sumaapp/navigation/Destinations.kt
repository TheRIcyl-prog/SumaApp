package com.suma.sumaapp.navigation

import androidx.navigation.NavController

sealed class Destinations(val route: String) {
    object launcchB : Destinations("launcchB")

    object login: Destinations("login")

    object mainScreen: Destinations("mainScreen")
    /*Для экранов с параметрами:
    object Details : Destinations("details/{itemId}") {
        fun createRoute(itemId: String) = "details/$itemId"
    }*/

}

fun NavController.navigateToLogin() {
    this.navigate(Destinations.login.route)
}

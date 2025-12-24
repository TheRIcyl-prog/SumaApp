package com.suma.sumaapp.navigation

sealed class Destinations(val route: String) {
    object Launch : Destinations("launch")
    object Login : Destinations("login")
    object Register : Destinations("register")
    object MainScreen : Destinations("main_screen")
    object ForgotPassword : Destinations("forgot_password")
}
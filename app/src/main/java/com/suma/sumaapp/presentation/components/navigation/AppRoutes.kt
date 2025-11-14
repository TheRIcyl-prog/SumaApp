package com.suma.sumaapp.presentation.components.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
}
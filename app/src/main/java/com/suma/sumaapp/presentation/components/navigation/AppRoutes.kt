package com.suma.sumaapp.presentation.components.navigation

sealed class AppRoutes(val route: String) {
    object Launch : AppRoutes("launch")
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
    object ForgotPassword : AppRoutes("forgot_password")
    object Main : AppRoutes("main")
    // Добавьте другие экраны по мере необходимости
}
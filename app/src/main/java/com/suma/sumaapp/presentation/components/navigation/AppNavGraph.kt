/*package com.suma.sumaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.suma.sumaapp.presentation.screens.launch.LauncchBScreen
import com.suma.sumaapp.presentation.screens.login.LoginScreen
import com.suma.sumaapp.presentation.screens.mainScreen.MainScreen
import com.suma.sumaapp.presentation.screens.register.RegisterScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.Launch.route
    ) {
        composable(Destinations.Launch.route) {
            LauncchBScreen(
                onLoginClick = {
                    navController.navigate(Destinations.Login.route)
                },
                onRegisterClick = {
                    navController.navigate(Destinations.Register.route)
                }
            )
        }

        composable(Destinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // После успешного входа переходим на главный экран
                    navController.navigate(Destinations.MainScreen.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    // Навигация на экран восстановления пароля
                    navController.navigate(Destinations.ForgotPassword.route)
                },
                onSwitchToSignup = {
                    navController.navigate(Destinations.Register.route)
                },
                onFingerprintClick = {
                    // Логика отпечатка пальца
                    println("Отпечаток пальца нажат")
                },
            )
        }

        composable(Destinations.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    // После успешной регистрации переходим на главный экран
                    navController.navigate(Destinations.MainScreen.route) {
                        popUpTo(Destinations.Register.route) { inclusive = true }
                    }
                },
                onSwitchToLogin = {
                    navController.navigate(Destinations.Login.route)
                }
            )
        }

        composable(Destinations.MainScreen.route) {
            MainScreen(
                onLogout = {
                    // Выход и возврат на экран входа
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(Destinations.MainScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.ForgotPassword.route) {
            // Экран восстановления пароля
            // ForgotPasswordScreen()
        }
    }
}*/
package com.suma.sumaapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suma.sumaapp.presentation.screens.launch.LauncchBScreen
import com.suma.sumaapp.presentation.screens.login.LoginScreen
import com.suma.sumaapp.presentation.screens.login.LoginViewModel
import com.suma.sumaapp.presentation.screens.mainScreen.MainScreen
import com.suma.sumaapp.presentation.screens.register.RegisterScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation() {
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
            // Создаем ViewModel для этого экрана
            val viewModel: LoginViewModel = viewModel()

            LoginScreen(
                onLoginSuccess = {
                    // После успешного входа переходим на главный экран
                    navController.navigate(Destinations.MainScreen.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Destinations.ForgotPassword.route)
                },
                onSwitchToSignup = {
                    navController.navigate(Destinations.Register.route)
                },
                onFingerprintClick = {
                    // TODO: Реализовать логику сканера отпечатка пальца
                    // Например:
                    // viewModel.loginWithFingerprint()
                    println("Отпечаток пальца нажат")
                },
                onLoginClick = {
                    // Этот параметр кажется лишним, так как у вас уже есть onLoginSuccess
                    // Вы можете либо удалить его из LoginScreen, либо передать пустую лямбду
                }
            )
        }

        composable(Destinations.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
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
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(Destinations.MainScreen.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
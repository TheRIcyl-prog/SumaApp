package com.suma.sumaapp.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.suma.sumaapp.presentation.screens.login.LoginScreen

import com.suma.sumaapp.presentation.screens.register.RegisterScreen


@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route, // Экран при запуске
        modifier = modifier
    ) {

        composable(AppRoutes.Login.route) {
            LoginScreen(
                onSwitchToSignup = { navController.navigate(AppRoutes.Register.route) }
            )
        }

        composable(AppRoutes.Register.route) {
            RegisterScreen(
                onSwitchToLogin = { navController.navigate(AppRoutes.Login.route) }
            )
        }
    }
}
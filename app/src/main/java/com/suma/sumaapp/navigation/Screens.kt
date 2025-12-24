/*package com.suma.sumaapp.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.suma.sumaapp.presentation.screens.launch.LauncchBScreen
import com.suma.sumaapp.presentation.screens.login.LoginScreen
import com.suma.sumaapp.presentation.screens.mainScreen.MainScreen
import com.suma.sumaapp.presentation.screens.register.RegisterScreen


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun Screens(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.launcchB.route
    ) {
        composable(Destinations.launcchB.route) {
            LauncchBScreen(navController = navController)
        }

        composable(Destinations.login.route) {
            LoginScreen(navController = navController)
        }

        composable(Destinations.login.route) {
            RegisterScreen(
                navController = navController,
                onCreateAccountClick = TODO(),
                onSwitchToLogin = TODO(),
                modifier = TODO()
            )
        }

        composable(Destinations.mainScreen.route) {
            MainScreen(navController = navController)
        }
    }
}*/
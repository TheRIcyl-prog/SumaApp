package com.suma.sumaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.suma.sumaapp.presentation.screens.launch.launcchBScreen


@Composable
fun Screens(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.launcchB.route
    ) {
        composable(Destinations.launcchB.route) {
            launcchBScreen(navController = navController)
        }
    }
}
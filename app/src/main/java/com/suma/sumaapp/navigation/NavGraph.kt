package com.suma.sumaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph (){
    val navController = rememberNavController()

    Screens(navController = navController)
}
package com.suma.sumaapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.suma.sumaapp.navigation.Destinations
import com.suma.sumaapp.presentation.screens.launch.LauncchBScreen
import com.suma.sumaapp.presentation.screens.login.LoginScreen
import com.suma.sumaapp.presentation.screens.login.LoginViewModel
import com.suma.sumaapp.presentation.screens.mainScreen.MainScreen
import com.suma.sumaapp.presentation.screens.register.RegisterScreen
import com.suma.sumaapp.ui.theme.SumaAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SumaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppWithAuthCheck()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppWithAuthCheck() {
    var isChecking by remember { mutableStateOf(true) }
    var initialRoute by remember { mutableStateOf(Destinations.Launch.route) }
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    // Проверяем авторизацию перед установкой NavHost
    LaunchedEffect(Unit) {
        delay(500) // Небольшая задержка для инициализации
        val currentUser = auth.currentUser

        // Устанавливаем начальный маршрут в зависимости от авторизации
        initialRoute = if (currentUser != null) {
            Destinations.MainScreen.route
        } else {
            Destinations.Launch.route
        }
        isChecking = false
    }

    if (isChecking) {
        // Показываем индикатор загрузки во время проверки
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                strokeWidth = 4.dp
            )
        }
    } else {
        // Устанавливаем NavHost с правильным начальным маршрутом
        NavHost(
            navController = navController,
            startDestination = initialRoute
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
                        navController.navigate(Destinations.MainScreen.route) {
                            popUpTo(Destinations.Login.route) { inclusive = true }
                        }
                    },
                    onForgotPasswordClick = {
                        // TODO: Реализовать экран восстановления пароля
                    },
                    onSwitchToSignup = {
                        navController.navigate(Destinations.Register.route)
                    },
                    onFingerprintClick = {
                        // TODO: Реализовать аутентификацию по отпечатку
                    },
                    onLoginClick = {}
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
}
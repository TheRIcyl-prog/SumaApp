package com.suma.sumaapp.presentation.screens.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.suma.sumaapp.R
import com.suma.sumaapp.ui.theme.CaribbeanGreen

@Composable
fun LauncсhBScreen(
    navController: NavController,
    viewModel: LaunchViewModel = viewModel()
) {
    val isLoading = viewModel.isLoading.collectAsState().value
    val lightBackgroundColor = Color(0xFFE8F5E9)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackgroundColor),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Иконка приложения
            Icon(
                painter = painterResource(id = R.drawable.ic_ico),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(120.dp),
                tint = CaribbeanGreen
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Название приложения
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = CaribbeanGreen
            )

            Text(
                text = "Управляйте своими финансами",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Кнопка "Вход"
            Button(
                onClick = {
                    viewModel.onLoginClick {
                        // Навигация на экран логина
                        navController.navigate("mainScreen")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CaribbeanGreen),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Вход",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Регистрация"
            OutlinedButton(
                onClick = {
                    viewModel.onRegisterClick {
                        // Навигация на экран регистрации
                        navController.navigate("register")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CaribbeanGreen,
                    containerColor = Color.Transparent
                ),
                border = ButtonDefaults.outlinedButtonBorder,
                enabled = !isLoading
            ) {
                Text(
                    text = "Регистрация",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Текст "Забыли пароль?"
            Text(
                text = "Забыли пароль?",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    viewModel.onForgotPasswordClick {
                        // Навигация на экран восстановления пароля
                        navController.navigate("forgot_password")
                    }
                }
            )
        }
    }
}
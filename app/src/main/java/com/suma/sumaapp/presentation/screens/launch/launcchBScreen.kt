package com.suma.sumaapp.presentation.screens.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import kotlinx.coroutines.delay

@Composable
fun LauncchBScreen(navController: NavController) {
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
                painter = painterResource(id = android.R.drawable.ic_dialog_info), // Замени на свою иконку
                contentDescription = "App Icon",
                modifier = Modifier.size(120.dp),
                tint = CaribbeanGreen
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Название приложения
            Text(
                text = "Suma",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = CaribbeanGreen
            )

            Text(
                text = "Приложение учёта финансов",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Кнопка "Вход"
            Button(
                onClick = {
                    //navController.navigateToLogin() // Навигация на логин
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CaribbeanGreen)
            ) {
                Text(
                    text = "Вход",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Регистрация"
            OutlinedButton(
                onClick = {
                    //navController.navigateToRegister() // Навигация на регистрацию
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
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    //color = CaribbeanGreen
                )
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
                    //navController.navigateToPasswordRecovery() // Навигация на восстановление
                }
            )
        }
    }

    // Автоматическая навигация через 3 секунды
    LaunchedEffect(Unit) {
        delay(3000) // 3 секунды
        //navController.navigateToLogin() // Автопереход на логин
    }
}

@Preview(showBackground = true)
@Composable
fun LauncchScreenPreview() {
    LauncchBScreen(navController = rememberNavController())
}

package com.suma.sumaapp.presentation.screens.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.suma.sumaapp.R
import com.suma.sumaapp.ui.theme.CaribbeanGreen

@Composable
fun LauncchBScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LaunchViewModel = viewModel()
) {
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
                contentDescription = stringResource(R.string.app_icon_description),
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
                text = stringResource(R.string.app_description),
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Кнопка "Вход"
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CaribbeanGreen)
            ) {
                Text(
                    text = stringResource(R.string.login_button),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Регистрация"
            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CaribbeanGreen,
                    containerColor = Color.Transparent
                ),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text(
                    text = stringResource(R.string.register_button),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Текст "Забыли пароль?"
            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    // Обработка "Забыли пароль"
                }
            )
        }
    }
}
package com.suma.sumaapp.presentation.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.presentation.components.fields.SumaTextField
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.SumaAppTheme

@Composable
fun RegisterScreen(
    onCreateAccountClick: () -> Unit = {},
    onSwitchToLogin: () -> Unit = {}
) {

    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val cardBg = Color(0xFFEFFDF6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CaribbeanGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Создать Аккаунт",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(26.dp))

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            color = cardBg
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                SumaTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Ваше Имя",
                    visible = true,
                    onToggleVisible = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                SumaTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    visible = true,
                    onToggleVisible = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                SumaTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "Номер Телефона",
                    visible = true,
                    onToggleVisible = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                SumaTextField(
                    value = birthday,
                    onValueChange = { birthday = it },
                    placeholder = "Дата Рождения",
                    visible = true,
                    onToggleVisible = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                SumaTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Пароль",
                    visible = passwordVisible,
                    onToggleVisible = { passwordVisible = !passwordVisible }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SumaTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Повторите пароль",
                    visible = confirmPasswordVisible,
                    onToggleVisible = { confirmPasswordVisible = !confirmPasswordVisible }
                )

                Spacer(modifier = Modifier.height(22.dp))


                Text(
                    text = "Продолжая, вы принимаете наши\nTerms of Use & Privacy Policy.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.6f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(36.dp))


                PrimaryButton(
                    text = "Регистрация",
                    onClick = onCreateAccountClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Уже есть аккаунт?  Войти",
                    fontSize = 15.sp,
                    modifier = Modifier.clickable { onSwitchToLogin() },
                    color = CaribbeanGreen,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterPreview() {
    SumaAppTheme {
        RegisterScreen()
    }
}

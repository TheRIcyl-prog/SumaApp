package com.suma.sumaapp.presentation.screens.login

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.presentation.components.buttons.SwitchTab
import com.suma.sumaapp.presentation.components.card.FloatCard
import com.suma.sumaapp.presentation.components.fields.SumaTextField
import com.suma.sumaapp.presentation.components.other.FingerprintButton
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.FenceGreen
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSwitchToSignup: () -> Unit,
    onFingerprintClick: () -> Unit,
    onLoginClick: Any
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }

    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current



    // Автоматический переход при успешном входе
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            focusManager.clearFocus()
            delay(800)
            onLoginSuccess()
        }
    }

    // Обработка ошибок
    if (uiState.error != null) {
        LaunchedEffect(uiState.error) {
            delay(5000)
            viewModel.clearError()
        }
    }

    // Заполняем email из истории
    LaunchedEffect(Unit) {
        val recentEmails = viewModel.getRecentEmails()
        if (recentEmails.isNotEmpty() && email.isEmpty()) {
            email = recentEmails.first()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "Добро пожаловать!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = FenceGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Введите ваши данные для входа",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Switch Login / Signup
                    SwitchTab(
                        options = listOf("Войти", "Зарегистрироваться"),
                        selectedIndex = 0,
                        onSelect = { index ->
                            if (index == 1) {
                                focusManager.clearFocus()
                                onSwitchToSignup()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // EMAIL
                    SumaTextField(
                        label = "Email",
                        value = email,
                        onValueChange = {
                            email = it
                            viewModel.updateEmail(it)
                            showPasswordError = false
                        },
                        placeholder = "example@mail.com",
                        password = false,
                        visible = false,
                        onToggleVisible = null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                        errorMessage = "Введите корректный email"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // PASSWORD
                    SumaTextField(
                        label = "Пароль",
                        value = password,
                        onValueChange = {
                            password = it
                            viewModel.updatePassword(it)
                            showPasswordError = false
                        },
                        placeholder = "Введите пароль",
                        password = true,
                        visible = isPasswordVisible,
                        onToggleVisible = { isPasswordVisible = !isPasswordVisible },
                        isError = (password.isNotBlank() && password.length < 6) || showPasswordError,
                        errorMessage = if (showPasswordError)
                            "Неверный пароль"
                        else if (password.isNotBlank() && password.length < 6)
                            "Пароль должен содержать минимум 6 символов"
                        else
                            ""
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Забыли пароль
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            modifier = Modifier.clickable {
                                focusManager.clearFocus()
                                onForgotPasswordClick()
                            },
                            text = "Забыли пароль?",
                            color = CaribbeanGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Показываем ошибку от ViewModel
                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    // Кнопка входа с индикатором загрузки
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = CaribbeanGreen,
                                strokeWidth = 3.dp
                            )
                        } else {
                            PrimaryButton(
                                text = "Войти",
                                onClick = {
                                    focusManager.clearFocus()
                                    if (isFormValid(email, password)) {
                                        viewModel.login(
                                            email = email,
                                            password = password,
                                            onSuccess = { userData ->
                                                println("Вход выполнен для: ${userData.email}")
                                            },
                                            onError = { errorMessage ->
                                                if (errorMessage.contains("пароль", ignoreCase = true)) {
                                                    showPasswordError = true
                                                }
                                                println("Ошибка входа: $errorMessage")
                                            }
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = isFormValid(email, password) && !uiState.isLoading
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Кнопка отпечатка пальца
                    FloatCard {
                        FingerprintButton(
                            onClick = {
                                focusManager.clearFocus()
                                onFingerprintClick()
                            }
                        )
                    }

                    // Ссылка на регистрацию
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Нет аккаунта? ",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            modifier = Modifier.clickable {
                                focusManager.clearFocus()
                                onSwitchToSignup()
                            },
                            text = "Зарегистрироваться",
                            color = CaribbeanGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

// Функция валидации формы
private fun isFormValid(email: String, password: String): Boolean {
    return email.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.isNotBlank() &&
            password.length >= 6
}
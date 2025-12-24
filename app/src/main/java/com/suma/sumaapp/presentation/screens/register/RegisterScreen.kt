package com.suma.sumaapp.presentation.screens.register

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suma.sumaapp.R
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.presentation.components.buttons.SwitchTab
import com.suma.sumaapp.presentation.components.card.FloatCard
import com.suma.sumaapp.presentation.components.fields.SumaTextField
import com.suma.sumaapp.presentation.components.other.Avatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onSwitchToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Автоматически переходим при успешной регистрации
    LaunchedEffect(uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            onRegisterSuccess()
        }
    }

    // Синхронизируем локальные состояния с ViewModel
    LaunchedEffect(name) { viewModel.updateName(name) }
    LaunchedEffect(email) { viewModel.updateEmail(email) }
    LaunchedEffect(password) { viewModel.updatePassword(password) }
    LaunchedEffect(confirmPassword) { viewModel.updateConfirmPassword(confirmPassword) }

    // Обработка ошибок
    if (uiState.error != null) {
        LaunchedEffect(uiState.error) {
            // Здесь можно показать Snackbar
            println("Ошибка: ${uiState.error}")
            // После 3 секунд очищаем ошибку
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                SwitchTab(
                    options = listOf("Войти", "Зарегистрироваться"),
                    selectedIndex = 1,
                    onSelect = { index ->
                        if (index == 0) {
                            onSwitchToLogin()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Avatar(image = R.drawable.ic_person)

                Spacer(modifier = Modifier.height(30.dp))

                // Поля ввода остаются без изменений
                SumaTextField(
                    label = "Имя",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Введите ваше имя",
                    password = false,
                    visible = false,
                    onToggleVisible = null,
                    isError = name.isNotBlank() && name.length < 2,
                    errorMessage = "Имя должно содержать минимум 2 символа"
                )

                Spacer(modifier = Modifier.height(18.dp))

                SumaTextField(
                    label = "E-Mail",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "example@mail.com",
                    password = false,
                    visible = false,
                    onToggleVisible = null,
                    isError = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                    errorMessage = "Введите корректный email"
                )

                Spacer(modifier = Modifier.height(18.dp))

                SumaTextField(
                    label = "Пароль",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Введите пароль",
                    password = true,
                    visible = isPasswordVisible,
                    onToggleVisible = { isPasswordVisible = !isPasswordVisible },
                    isError = password.isNotBlank() && password.length < 6,
                    errorMessage = "Пароль должен содержать минимум 6 символов"
                )

                Spacer(modifier = Modifier.height(18.dp))

                SumaTextField(
                    label = "Подтвердите пароль",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Повторите пароль",
                    password = true,
                    visible = isConfirmPasswordVisible,
                    onToggleVisible = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                    isError = confirmPassword.isNotBlank() && password != confirmPassword,
                    errorMessage = "Пароли не совпадают"
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Показываем индикатор загрузки или кнопку
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    PrimaryButton(
                        text = "Зарегистрироваться",
                        onClick = {
                            viewModel.register(
                                onSuccess = {
                                    // Успех обрабатывается через LaunchedEffect выше
                                },
                                onError = { errorMessage ->
                                    println("Ошибка: $errorMessage")
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid(name, email, password, confirmPassword)
                    )
                }

                // Показываем ошибку, если есть
                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                FloatCard {
                    Text(
                        text = "Регистрируясь, вы соглашаетесь с Политикой конфиденциальности",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

private fun isFormValid(
    name: String,
    email: String,
    password: String,
    confirmPassword: String
): Boolean {
    return name.length >= 2 &&
            Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.length >= 6 &&
            password == confirmPassword
}
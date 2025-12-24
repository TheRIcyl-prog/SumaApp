package com.suma.sumaapp.presentation.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onCreateAccountClick: () -> Unit = {},
    onSwitchToLogin: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

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
                if (index == 0) onSwitchToLogin()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Avatar(image = R.drawable.ic_person)

        Spacer(modifier = Modifier.height(30.dp))

        SumaTextField(
            label = "Имя",
            value = uiState.name,
            onValueChange = { viewModel.updateName(it) }
        )

        Spacer(modifier = Modifier.height(18.dp))

        SumaTextField(
            label = "E-Mail",
            value = uiState.email,
            onValueChange = { viewModel.updateEmail(it) }
        )

        Spacer(modifier = Modifier.height(18.dp))

        SumaTextField(
            label = "Пароль",
            value = uiState.password,
            password = true,
            onValueChange = { viewModel.updatePassword(it) }
        )

        Spacer(modifier = Modifier.height(18.dp))

        SumaTextField(
            label = "Подтвердите пароль",
            value = uiState.confirmPassword,
            password = true,
            onValueChange = { viewModel.updateConfirmPassword(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = if (uiState.isLoading) "Регистрация..." else "Зарегистрироваться",
            enabled = !uiState.isLoading,
            onClick = {
                viewModel.register(
                    onSuccess = onCreateAccountClick,
                    onError = { error ->
                        // Можно показать Snackbar или Toast
                    }
                )
            },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(20.dp))

        FloatCard {
            Text(
                text = "Регистрируясь, вы соглашаетесь с Политикой конфеденциальности",
                color = Color.Gray,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
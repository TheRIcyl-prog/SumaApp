package com.suma.sumaapp.presentation.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.suma.sumaapp.R
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.presentation.components.buttons.SwitchTab
import com.suma.sumaapp.presentation.components.card.FloatCard
import com.suma.sumaapp.presentation.components.fields.SumaTextField
import com.suma.sumaapp.presentation.components.other.Avatar


@Composable
fun RegisterScreen(
    onCreateAccountClick: () -> Unit = {},
    onSwitchToLogin: () -> Unit = {}
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
            onSelect = { onSwitchToLogin() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Avatar(image = R.drawable.ic_person)

        Spacer(modifier = Modifier.height(30.dp))

        SumaTextField(
            label = "Имя",
            value = "",
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(18.dp))

        SumaTextField(
            label = "E-Mail",
            value = "",
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(18.dp))

        SumaTextField(
            label = "Пароль",
            value = "",
            password = true,
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(18.dp))

        SumaTextField(
            label = "Подтвердите пароль",
            value = "",
            password = true,
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Зарегистрироваться",
            onClick = onCreateAccountClick,
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

package com.suma.sumaapp.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.presentation.components.buttons.SwitchTab
import com.suma.sumaapp.presentation.components.card.FloatCard
import com.suma.sumaapp.presentation.components.fields.SumaTextField
import com.suma.sumaapp.presentation.components.other.FingerprintButton
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.FenceGreen

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSwitchToSignup: () -> Unit = {},
    onFingerprintClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text( text = "Добро пожаловать!",
            fontSize = 24.sp,
            color = FenceGreen
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Switch Login / Signup
        SwitchTab(
            options = listOf("Login", "Sign Up"),
            selectedIndex = 0,
            onSelect = { onSwitchToSignup() }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // EMAIL
        SumaTextField(
            label = "Email",
            value = "",
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        // PASSWORD
        SumaTextField(
            label = "Password",
            value = "",
            onValueChange = {},
            password = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPasswordClick() },
            text = "Forgot password?",
            color = CaribbeanGreen
        )

        Spacer(modifier = Modifier.height(30.dp))

        PrimaryButton(
            text = "Log In",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(40.dp))

        FloatCard {
            FingerprintButton(onClick = onFingerprintClick)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


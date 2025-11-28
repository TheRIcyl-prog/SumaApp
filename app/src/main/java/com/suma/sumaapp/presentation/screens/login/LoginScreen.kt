package com.suma.sumaapp.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.suma.sumaapp.R
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.presentation.components.card.FloatCard
import com.suma.sumaapp.presentation.components.fields.SumaTextField
import com.suma.sumaapp.presentation.components.other.FingerprintButton
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.SumaAppTheme

/**
 * Login screen UI matched to provided design.
 *
 * - Uses SumaTextField for email
 * - Provides custom PasswordField (visual only) to match pill shape + eye toggle
 * - Uses PrimaryButton for the main action
 *
 * Other developer should supply real logic via callbacks.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: (String, String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onSwitchToSignup: () -> Unit = {},
    navController: NavController? = null,
    onFingerprintClick: () -> Unit = {}
) {
    // local state for preview/UI — real logic should replace these with ViewModel states
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Colors that mimic the design (adjust in your theme if needed)
    val headerColor = CaribbeanGreen // main top area
    val cardBg = Color(0xFFEFFDF6) // inner rounded card background (very light green)
    val pillBg = Color(0xFFDFF6ED) // pill shape inside card
    val placeholderColor = Color(0xFFBFCFC3)
    val hintTextSize = 14.sp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(headerColor) // full-screen top green; we'll place inner card overlapping
    ) {
        // top spacing (status bar area)
        Spacer(modifier = Modifier.height(22.dp))

        // Welcome text centered at top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Добро пожаловать!",
                fontSize = 28.sp,
                color = Color(0xFF00221A), // dark green/blackish
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp),
                textAlign = TextAlign.Center
            )
        }

        // The big rounded card area (white/light green) — placed inside the green background
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = cardBg,
            shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
        ) {
            // content inside card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                // Email - use SumaTextField from ui-kit
                SumaTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    visible = true,
                    onToggleVisible = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password — pill-shaped field with eye toggle to match design
                PasswordPillField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Пароль",
                    visible = passwordVisible,
                    onToggleVisible = { passwordVisible = !passwordVisible }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Primary action
                PrimaryButton(
                    text = "Войти",
                    onClick = { onLogin(email, password) },
                )

                Spacer(modifier = Modifier.height(8.dp))

                // "Forgot password?" as small link
                Text(
                    text = "Забыли пароль?",
                    color = Color(0xFF1E6A5A),
                    modifier = Modifier
                        .clickable { onForgotPassword() }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Register secondary pill button — light background with darker text
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(pillBg)
                        .clickable { onSwitchToSignup() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Регистрация", color = Color(0xFF0E6A54), fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(22.dp))

                // "or sign up with" + social icons
                Text(text = "или войти через", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    // placeholders for social icons — replace painterResource with real drawables
                    SocialCircleIcon(resId = R.drawable.ic_facebook)
                    Spacer(modifier = Modifier.size(18.dp))
                    SocialCircleIcon(resId = R.drawable.ic_google)
                }
            }
        }
    }
}

/**
 * Password pill-shaped field (visual only).
 * Uses similar appearance to SumaTextField but shows an interactive eye icon.
 */
@Composable
private fun PasswordPillField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visible: Boolean,
    onToggleVisible: () -> Unit
) {
    // mirror style from SumaTextField (pill / rounded)
    val fieldShape = RoundedCornerShape(28.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(fieldShape)
            .background(Color(0xFFDFF6ED))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Use a TextField visually but without visible indicator (to keep consistent look)
        androidx.compose.material3.TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = Color(0xFF9FB9A8)) },
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
//            trailingIcon = {
//                Icon(
//                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                    contentDescription = "Toggle password",
//                    modifier = Modifier
//                        .size(22.dp)
//                        .clickable { onToggleVisible() }
//                )
//            },
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFDFF6ED),
                unfocusedContainerColor = Color(0xFFDFF6ED),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun SocialCircleIcon(resId: Int) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF6FFF6)),
        contentAlignment = Alignment.Center
    ) {
        // replace with painterResource icons in res/drawable
        androidx.compose.material3.Icon(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SumaAppTheme {
        LoginScreen(
            onLogin = { _, _ -> },
            onForgotPassword = {},
            onSwitchToSignup = {},
            navController = null,
            onFingerprintClick = {}
        )
    }
}


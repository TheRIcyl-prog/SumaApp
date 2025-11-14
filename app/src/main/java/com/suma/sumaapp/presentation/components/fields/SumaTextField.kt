package com.suma.sumaapp.presentation.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suma.sumaapp.ui.theme.CaribbeanGreen

@Composable
fun SumaTextField(
    label: String,
    icon: Int? = null,
    value: String,
    onValueChange: (String) -> Unit,
    password: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(1.dp, Color(0xFFDAF5E3), RoundedCornerShape(12.dp))
            .background(Color(0xFFEFFDF6), RoundedCornerShape(12.dp)),
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
        singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        placeholder = { Text(label, color = Color.Gray) },
        trailingIcon = {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = CaribbeanGreen
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFEFFDF6),
            unfocusedContainerColor = Color(0xFFEFFDF6),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
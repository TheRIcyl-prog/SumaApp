package com.suma.sumaapp.presentation.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun SumaTextField(
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
            .clip(fieldShape)
            .background(Color(0xFFDFF6ED))
            .padding(horizontal = 5.dp),
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
//            if (icon != null) {
//                Icon(
//                    painter = painterResource(icon),
//                    contentDescription = null,
//                    tint = CaribbeanGreen
//                )
//            }
//          },
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFDFF6ED),
                unfocusedContainerColor = Color(0xFFDFF6ED),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
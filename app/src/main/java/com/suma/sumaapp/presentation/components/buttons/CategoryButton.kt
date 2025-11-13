package com.suma.sumaapp.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.suma.sumaapp.ui.theme.Honeydew
import com.suma.sumaapp.ui.theme.LightBlue
import com.suma.sumaapp.ui.theme.LightGreen
import com.suma.sumaapp.ui.theme.OceanBlue
import com.suma.sumaapp.ui.theme.VividBlue

@Composable
fun CategoryButton(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    isPrimary: Boolean = true
) {
    val backgroundColor = when {
        selected && isPrimary -> OceanBlue
        !selected && isPrimary -> LightBlue
        selected && !isPrimary -> VividBlue
        else -> Honeydew
    }

    Box(
        modifier = modifier
            .size(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}
package com.suma.sumaapp.presentation.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SumaChart() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .background(Color(0xFFDFFFF2), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Заглушка диаграмм")
    }
}

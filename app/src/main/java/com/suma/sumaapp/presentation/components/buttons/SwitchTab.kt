package com.suma.sumaapp.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.suma.sumaapp.ui.theme.CaribbeanGreen

@Composable
fun SwitchTab(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color(0xFFE8F9EF), shape = RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        options.forEachIndexed { index, text ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) CaribbeanGreen else Color.Transparent)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = if (selected) White else CaribbeanGreen
                )
            }
        }
    }
}

package com.suma.sumaapp.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suma.sumaapp.ui.theme.CaribbeanGreen


@Composable
fun NavigationBar(
    items: List<Int>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F9EF), shape = RoundedCornerShape(30.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEachIndexed { index, iconId ->
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                tint = if (selectedIndex == index) CaribbeanGreen else Color.Gray,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onItemClick(index) }
            )
        }
    }
}

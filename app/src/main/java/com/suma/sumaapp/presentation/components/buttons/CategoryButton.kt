package com.suma.sumaapp.presentation.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.R
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

// Превью с разными состояниями кнопки
@Preview(showBackground = true)
@Composable
fun CategoryButtonPreview() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Primary Buttons", color = Color.Black)
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Primary не выбранная
            CategoryButton(
                icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = false,
                isPrimary = true
            )

            // Primary выбранная
            CategoryButton(
                icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = true,
                isPrimary = true
            )
        }

        Text("Secondary Buttons", color = Color.Black)
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Secondary не выбранная
            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.Home),
                onClick = {},
                selected = false,
                isPrimary = false
            )

            // Secondary выбранная
            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.Home),
                onClick = {},
                selected = true,
                isPrimary = false
            )
        }

        Text("Different Icons", color = Color.Black)
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategoryButton(
                icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = true,
                isPrimary = true
            )

            CategoryButton(
                icon =painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = false,
                isPrimary = true
            )

            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.ShoppingCart),
                onClick = {},
                selected = true,
                isPrimary = false
            )

            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.Home),
                onClick = {},
                selected = false,
                isPrimary = false
            )
        }
    }
}

// Простое превью одной кнопки
@Preview(showBackground = true)
@Composable
fun SingleCategoryButtonPreview() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CategoryButton(
            icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
            onClick = { },
            selected = true,
            isPrimary = true
        )
    }
}

// Превью всех состояний в сетке
@Preview(showBackground = true, widthDp = 360)
@Composable
fun AllCategoryButtonsPreview() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "Category Button States",
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Primary кнопки
        Text("Primary", color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            CategoryButton(
                icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = false,
                isPrimary = true
            )
            CategoryButton(
                icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = true,
                isPrimary = true
            )
        }

        // Secondary кнопки
        Text("Secondary", color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.Home),
                onClick = {},
                selected = false,
                isPrimary = false
            )
            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.Home),
                onClick = {},
                selected = true,
                isPrimary = false
            )
        }

        // Разные иконки
        Text("Different Categories", color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategoryButton(
                icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = true,
                isPrimary = true
            )
            CategoryButton(
                icon = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico),
                onClick = {},
                selected = false,
                isPrimary = true
            )
            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.ShoppingCart),
                onClick = {},
                selected = true,
                isPrimary = false
            )
            CategoryButton(
                icon = rememberVectorPainter(Icons.Default.Home),
                onClick = {},
                selected = false,
                isPrimary = false
            )
        }
    }
}
package com.suma.sumaapp.presentation.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.OceanBlue
import com.suma.sumaapp.ui.theme.VividBlue

@Composable
fun CategoryItemRow(
    iconPainter: Painter,
    categoryName: String,
    period: String,
    amount: String,
    modifier: Modifier = Modifier,
    iconBackgroundColor: Color = OceanBlue,
    primaryTextColor: Color = Color.Black,
    secondaryTextColor: Color = Color.Blue,
    dividerColor: Color = CaribbeanGreen
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = iconPainter,
                    contentDescription = categoryName,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = categoryName,
                    color = primaryTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = period,
                    color = secondaryTextColor,
                    fontSize = 12.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .height(32.dp)
                .width(1.dp)
                .background(dividerColor)
        )

        Text(
            text = amount,
            color = secondaryTextColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemRowPreview() {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CategoryItemRow(
            iconPainter = androidx.compose.ui.res.painterResource(android.R.drawable.ic_btn_speak_now),
            categoryName = "Продукты",
            period = "1 - 24 апреля",
            amount = "-100,00 ₽",
            iconBackgroundColor = OceanBlue
        )

        CategoryItemRow(
            iconPainter = androidx.compose.ui.res.painterResource(android.R.drawable.ic_dialog_info),
            categoryName = "Дом",
            period = "Весь месяц",
            amount = "-550,50 ₽",
            iconBackgroundColor = VividBlue
        )
    }
}
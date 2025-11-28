package com.suma.sumaapp.presentation.components.items // Изменил пакет для лучшей организации

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider // Для вертикальной линии
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suma.sumaapp.ui.theme.*

@Composable
fun CategoryItemRow(
    iconPainter: Painter,
    categoryName: String,
    period: String,
    amount: String,
    modifier: Modifier = Modifier,
    iconBackgroundColor: Color = OceanBlue, // Цвет фона иконки
    primaryTextColor: Color = Color.Black, // Основной цвет текста
    secondaryTextColor: Color = Color.Blue, // Цвет периода и суммы
    dividerColor: Color = CaribbeanGreen // Цвет вертикальной линии
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White) // Фон всей строки, если нужно
            .padding(16.dp), // Отступы вокруг всей строки
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Распределяем элементы по краям
    ) {
        // Левая часть: иконка и текст
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Круглая иконка
            Box(
                modifier = Modifier
                    .size(48.dp) // Размер круга
                    .clip(CircleShape)
                    .background(iconBackgroundColor)
                    .padding(8.dp), // Отступ внутри круга для иконки
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = iconPainter,
                    contentDescription = categoryName,
                    tint = Color.White, // Цвет иконки
                    modifier = Modifier.size(24.dp) // Размер самой иконки
                )
            }

            // Текстовая информация: название и период
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

        // Центральная часть: разделитель
        // Divider(
        //     color = dividerColor,
        //     modifier = Modifier
        //         .height(32.dp) // Высота линии
        //         .width(1.dp)   // Толщина линии
        // )
        // Из фото видно, что разделитель не занимает всю высоту, а скорее является небольшим элементом
        // Для простоты можно оставить его как есть или убрать, если он не важен.
        // Если нужен именно тонкий вертикальный элемент:
        Box(
            modifier = Modifier
                .height(32.dp)
                .width(1.dp)
                .background(dividerColor)
        )


        // Правая часть: сумма
        Text(
            text = amount,
            color = secondaryTextColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Превью компонента
@Preview(showBackground = true, backgroundColor = 0xFFF0FFF0) // Светло-зеленый фон для превью
@Composable
fun CategoryItemRowPreview() {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Пример с продуктами
        CategoryItemRow(
            iconPainter = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico), // Замените на ваш ресурс иконки продуктов
            categoryName = "Продукты",
            period = "1 - 24 апреля",
            amount = "-$100,00"
        )

        // Другой пример
        CategoryItemRow(
            iconPainter = painterResource(id = com.suma.sumaapp.R.drawable.ic_ico), // Пример другой иконки
            categoryName = "Дом",
            period = "Весь месяц",
            amount = "-$550,50",
            iconBackgroundColor = VividBlue,
            secondaryTextColor = VividBlue
        )
    }
}
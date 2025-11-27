package com.suma.sumaapp.presentation.screens.mainScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.suma.sumaapp.R
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.Honeydew

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.drawscope.Stroke


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    Column(
        modifier = Modifier.background(color = CaribbeanGreen).fillMaxSize()
    ) {
        Box(modifier = Modifier
            .background(
                color = CaribbeanGreen,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .fillMaxWidth()
            .weight(0.9f)){

            Text(
                text = "Приветики пистолетики"
            )
        }
        Column(
            modifier = Modifier
                .background(
                    color = Honeydew,
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                )
                .fillMaxWidth()
                .weight(2f)
        ){
            val expenseSegments = listOf(
                CircleSegment(Color(0xFFFF6B6B), 1500f, "Еда"),
                CircleSegment(Color(0xFF4ECDC4), 8000f, "Транспорт"),
                CircleSegment(Color(0xFFFFD166), 5000f, "Развлечения"),
                CircleSegment(Color(0xFF6A0572), 12000f, "Жилье"),
                        CircleSegment((CaribbeanGreen), 15000f, "Еда")
            )

            // Диаграмма по центру
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ProgressCircle(
                    segments = expenseSegments,
                    size = 320.dp,
                    strokeWidth = 24.dp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                )
            }


            PrimaryButton(
                text = stringResource(
                    R.string.new_trata),
                onClick = {},
                modifier = Modifier
                    .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
            )



        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}




data class CircleSegment(
    val color: Color,
    val value: Float, // значение от 0 до 1
    val label: String = ""
)

@Composable
fun ProgressCircle(
    segments: List<CircleSegment>,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 20.dp,
    showBackground: Boolean = true,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f)
) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()

    Canvas(
        modifier = modifier.size(size)
    ) {
        val canvasWidth = size.toPx()
        val canvasHeight = size.toPx()
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val radius = (canvasWidth.coerceAtMost(canvasHeight) - strokeWidth.toPx()) / 2

        // Фон круга
        if (showBackground) {
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = strokeWidth.toPx())
            )
        }

        // Отрисовка сегментов
        var startAngle = -90f // Начинаем с верха (12 часов)

        segments.forEach { segment ->
            val sweepAngle = 360f * (segment.value / totalValue)

            drawArc(
                color = segment.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(centerX - radius, centerY - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth.toPx())
            )

            startAngle += sweepAngle
        }
    }
}
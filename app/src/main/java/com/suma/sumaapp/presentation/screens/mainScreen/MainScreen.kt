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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.suma.sumaapp.R
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.Honeydew
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val expenseSegments = remember {
        listOf(
            CircleSegment(Color(0xFFFF6B6B), 1500f, "Еда"),
            CircleSegment(Color(0xFF4ECDC4), 8000f, "Транспорт"),
            CircleSegment(Color(0xFFFFD166), 5000f, "Развлечения"),
            CircleSegment(Color(0xFF6A0572), 12000f, "Жилье"),
            CircleSegment(CaribbeanGreen, 15000f, "Другое")
        )
    }

    Column(
        modifier = Modifier
            .background(color = CaribbeanGreen)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = CaribbeanGreen,
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                )
                .fillMaxWidth()
                .weight(0.5f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Приветики пистолетики",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
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
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .padding(end = 10.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CaribbeanGreen),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ProgressCircleWithText(
                            segments = expenseSegments,
                            centerText = stringResource(R.string.Ostatoc),
                            centerSubtext = "40 000 ₽",
                            size = 200.dp,
                            strokeWidth = 28.dp
                        )
                    }
                }

                CategoryLegend(
                    categories = expenseSegments,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            PrimaryButton(
                text = stringResource(R.string.new_trata),
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )
        }
    }
}

@Composable
fun ProgressCircleWithText(
    segments: List<CircleSegment>,
    centerText: String,
    centerSubtext: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 20.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        ProgressCircle(
            segments = segments,
            modifier = Modifier.fillMaxSize(),
            size = size,
            strokeWidth = strokeWidth
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = centerText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = centerSubtext,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }
    }
}

@Composable
private fun CategoryLegend(
    categories: List<CircleSegment>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(category.color, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = category.label,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = "${category.value.toInt()} ₽",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
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
    val value: Float,
    val label: String = ""
)

@Composable
fun ProgressCircle(
    segments: List<CircleSegment>,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 20.dp,
    borderWidth: Dp = 3.dp,
    gapWidthDegrees: Float = 2f,
    showBackground: Boolean = true,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f)
) {
    val totalValue = segments.sumOf { it.value.toDouble() }.toFloat()

    Canvas(
        modifier = modifier.size(size)
    ) {
        val radius = (size.toPx() - strokeWidth.toPx()) / 2
        val center = this.center

        if (showBackground) {
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth.toPx())
            )
        }

        var startAngle = -90f

        segments.forEach { segment ->
            val sweepAngle = 360f * (segment.value / totalValue)
            val adjustedSweep = sweepAngle - gapWidthDegrees

            if (adjustedSweep > 0f) {
                drawArc(
                    color = Color.White,
                    startAngle = startAngle,
                    sweepAngle = adjustedSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth.toPx() + borderWidth.toPx())
                )

                drawArc(
                    color = segment.color,
                    startAngle = startAngle,
                    sweepAngle = adjustedSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth.toPx())
                )
            }

            drawArc(
                color = Color.White,
                startAngle = startAngle + adjustedSweep,
                sweepAngle = gapWidthDegrees,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth.toPx())
            )

            startAngle += sweepAngle
        }
    }
}
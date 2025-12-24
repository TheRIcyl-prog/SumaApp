package com.suma.sumaapp.presentation.screens.mainScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.suma.sumaapp.presentation.components.items.CategoryItemRow
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(
    navController: NavController
) {
    val viewModel: MainViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val expenseSegments = uiState.categories

    Column(
        modifier = Modifier
            .background(CaribbeanGreen)
            .fillMaxSize()
    ) {
        // Верхняя часть — приветствие
        Box(
            modifier = Modifier
                .background(
                    CaribbeanGreen,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .fillMaxWidth()
                .weight(0.5f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Мои расходы",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }

        // Нижняя часть
        Column(
            modifier = Modifier
                .background(
                    Honeydew,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ================================
                //    КАРТОЧКА С ДИАГРАММОЙ
                // ================================
                Card(
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CaribbeanGreen),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Диаграмма (фиксированная)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            ProgressCircleWithText(
                                segments = expenseSegments,
                                centerText = stringResource(R.string.Ostatoc),
                                centerSubtext = "${expenseSegments.sumOf { it.value.toDouble() }.toInt()} ₽",
                                size = 200.dp,
                                strokeWidth = 28.dp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // ================================
                        //   ЛЕГЕНДА (если много — скролл)
                        // ================================
                        if (expenseSegments.isNotEmpty()) {
                            if (expenseSegments.size > 3) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(expenseSegments) { segment ->
                                        CategoryLegendItem(segment)
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    expenseSegments.forEach { segment ->
                                        CategoryLegendItem(segment)
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Добавьте первую трату",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ================================
                //   НИЖНИЙ СПИСОК CATEGORY ITEM
                // ================================
                if (expenseSegments.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                    ) {
                        items(expenseSegments) { segment ->
                            CategoryItemRow(
                                iconPainter = painterResource(id = R.drawable.ic_ico),
                                categoryName = segment.label,
                                period = getCurrentPeriod(),
                                amount = "-${segment.value.toInt()} ₽",
                                iconBackgroundColor = segment.color
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Нет добавленных трат",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            }

            PrimaryButton(
                text = stringResource(R.string.new_trata),
                onClick = { viewModel.showAddExpenseSheet() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )
        }
    }

    // Bottom Sheet для добавления траты
    if (uiState.showAddExpenseSheet) {
        AddExpenseBottomSheet(
            onDismiss = { viewModel.hideAddExpenseSheet() },
            onAddExpense = { categoryName, amount ->
                viewModel.addExpense(categoryName, amount)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseBottomSheet(
    onDismiss: () -> Unit,
    onAddExpense: (String, Float) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
        modifier = Modifier.fillMaxHeight(0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Заголовок
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Добавить трату",
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Закрыть")
                }
            }

            // Поле для названия категории
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Название категории") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Поле для суммы
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    if (it.matches(Regex("^\\d*\\.?\\d*$")) || it.isEmpty()) {
                        amount = it
                    }
                },
                label = { Text("Сумма") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("0.00") }
            )

            // Кнопка добавления
            PrimaryButton(
                text = "Добавить трату",
                onClick = {
                    if (categoryName.isNotBlank() && amount.isNotBlank()) {
                        onAddExpense(categoryName, amount.toFloat())
                        categoryName = ""
                        amount = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CategoryLegendItem(category: CircleSegment) {
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
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        Text(
            text = "${category.value.toInt()} ₽",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
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
    val totalValue = if (segments.isNotEmpty()) segments.sumOf { it.value.toDouble() }.toFloat() else 1f

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

        if (segments.isNotEmpty()) {
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
}

private fun getCurrentPeriod(): String {
    val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
    val currentDate = Calendar.getInstance()
    val startOfMonth = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }
    return "${dateFormat.format(startOfMonth.time)} — ${dateFormat.format(currentDate.time)}"
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
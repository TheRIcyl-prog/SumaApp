package com.suma.sumaapp.presentation.screens.mainScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suma.sumaapp.R
import com.suma.sumaapp.presentation.components.buttons.PrimaryButton
import com.suma.sumaapp.presentation.components.items.CategoryItemRow
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import com.suma.sumaapp.ui.theme.Honeydew
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    val viewModel: MainViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val expenseSegments = uiState.categories

    // Состояния для выпадающего меню баланса
    var showBalanceSheet by remember { mutableStateOf(false) }
    var sheetOffset by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    // Диалог очистки всех расходов
    var showClearDialog by remember { mutableStateOf(false) }

    // Загружаем данные при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CaribbeanGreen),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CaribbeanGreen)
    ) {
        // Верхняя часть с балансом (сверху)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(CaribbeanGreen)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Приветствие
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Привет, ${uiState.userName}!",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        text = "Управляй своими финансами",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Row {
                    // Кнопка очистки всех расходов
                    IconButton(
                        onClick = { showClearDialog = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Очистить все",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Кнопка профиля
                    IconButton(
                        onClick = { /* Открыть профиль */ },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Профиль",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Карточка с балансом (с возможностью свайпа вниз)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = { isDragging = false },
                            onVerticalDrag = { change, dragAmount ->
                                if (dragAmount > 0 && sheetOffset >= 50f) {
                                    showBalanceSheet = true
                                    sheetOffset = 0f
                                }
                            }
                        )
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clickable { showBalanceSheet = true },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(

                        text = "Текущий баланс",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${uiState.balance.roundToInt()} ₽",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val progress = if (uiState.balance > 0) {
                        uiState.remainingBalance / uiState.balance
                    } else {
                        0f
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = Color(0xFF4CAF50),
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Осталось: ${uiState.remainingBalance.roundToInt()} ₽",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        Text(
                            text = "Потрачено: ${uiState.totalExpenses.roundToInt()} ₽",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Основной контент (снизу)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 280.dp)
                .background(
                    Honeydew,
                    RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
        ) {
            // Заголовок периода с ОСТАТКОМ (баланс - траты)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Сегодня: ${uiState.currentDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                    Text(
                        text = getCurrentPeriod(),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )
                }

                // Изменено: вместо "Итого" теперь "Остаток: баланс - траты"
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Остаток: ${uiState.remainingBalance.roundToInt()} ₽",
                        style = MaterialTheme.typography.titleMedium,
                        color = CaribbeanGreen
                    )
                    Text(
                        text = "${uiState.balance.roundToInt()} - ${uiState.totalExpenses.roundToInt()} ₽",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Карточка с диаграммой (уменьшены отступы)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Уменьшен padding с 24dp до 16dp
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Диаграмма (уменьшены размеры и отступы)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Уменьшена высота
                            .padding(8.dp), // Уменьшен padding
                        contentAlignment = Alignment.Center
                    ) {
                        ProgressCircleWithText(
                            segments = expenseSegments,
                            centerText = "Траты",
                            centerSubtext = "${uiState.totalExpenses.roundToInt()} ₽",
                            size = 180.dp, // Уменьшен размер
                            strokeWidth = 28.dp // Уменьшена толщина
                        )
                    }

                    // Легенда категорий
                    if (expenseSegments.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            expenseSegments.forEachIndexed { index, segment ->
                                AnimatedLegendItem(
                                    segment = segment,
                                    index = index,
                                    onDelete = { viewModel.removeExpense(segment.id) }
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp), // Уменьшена высота
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "📊",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = "Нет трат",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Подсказка для удаления
            if (expenseSegments.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CaribbeanGreen.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Информация",
                            tint = CaribbeanGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Нажмите на категорию для удаления",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Список категорий
            if (expenseSegments.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenseSegments.sortedByDescending { it.value }) { segment ->
                        SwipeToDeleteCategoryItem(
                            segment = segment,
                            onDelete = { viewModel.removeExpense(segment.id) }
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "💳",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Нет добавленных трат",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = "Добавьте траты, чтобы увидеть статистику",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            }

            // Кнопка добавления
            PrimaryButton(
                text = stringResource(R.string.new_trata),
                onClick = { viewModel.showAddExpenseSheet() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }

    // Bottom Sheet для изменения баланса
    if (showBalanceSheet) {
        BalanceBottomSheet(
            currentBalance = uiState.balance,
            onDismiss = {
                showBalanceSheet = false
                sheetOffset = 0f
            },
            onSaveBalance = { newBalance ->
                viewModel.updateBalance(newBalance)
                showBalanceSheet = false
                sheetOffset = 0f
            }
        )
    }

    // Диалог очистки всех расходов
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Очистить все траты?") },
            text = { Text("Это действие удалит все ваши траты. Вы не сможете их восстановить.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllExpenses()
                        showClearDialog = false
                    }
                ) {
                    Text("Очистить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
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

// Компонент с возможностью свайпа для удаления
@Composable
fun SwipeToDeleteCategoryItem(
    segment: CircleSegment,
    onDelete: () -> Unit
) {
    var swipeOffset by remember { mutableStateOf(0f) }
    var isSwiped by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        // Фон удаления (красный)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red, RoundedCornerShape(12.dp))
                .padding(end = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Удалить",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Основная карточка
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = swipeOffset.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        if (!isSwiped) {
                            swipeOffset = (swipeOffset + dragAmount).coerceIn(-150f, 0f)
                            if (swipeOffset < -100f && !isSwiped) {
                                isSwiped = true
                                onDelete()
                            }
                        }
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(segment.color, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = segment.label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                        Text(
                            text = "${segment.percent.roundToInt()}% • ${segment.date}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                Text(
                    text = "-${segment.value.roundToInt()} ₽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun AnimatedLegendItem(
    segment: CircleSegment,
    index: Int,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = {
            onDelete()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(segment.color, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = segment.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                        Text(
                            text = "${segment.percent.roundToInt()}% • ${segment.date}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${segment.value.roundToInt()} ₽",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "Нажмите для удаления",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Red.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Обновленный Bottom Sheet для изменения баланса
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceBottomSheet(
    currentBalance: Float,
    onDismiss: () -> Unit,
    onSaveBalance: (Float) -> Unit
) {
    var newBalance by remember { mutableStateOf(currentBalance.toString()) }
    var error by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
        modifier = Modifier.fillMaxHeight(0.4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Заголовок
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Изменить баланс",
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Закрыть")
                }
            }

            // Текущий баланс
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CaribbeanGreen.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Текущий баланс",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "${currentBalance.roundToInt()} ₽",
                        style = MaterialTheme.typography.headlineMedium,
                        color = CaribbeanGreen
                    )
                }
            }

            // Поле ввода
            OutlinedTextField(
                value = newBalance,
                onValueChange = {
                    if (it.matches(Regex("^\\d*\\.?\\d*$")) || it.isEmpty()) {
                        newBalance = it
                        error = false
                    }
                },
                label = { Text("Новый баланс") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Введите сумму") },
                isError = error,
                supportingText = {
                    if (error) {
                        Text("Введите корректную сумму")
                    }
                },
                leadingIcon = {
                    Text(
                        text = "₽",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )

            // Кнопка сохранения
            Button(
                onClick = {
                    val amount = newBalance.toFloatOrNull()
                    if (amount != null && amount >= 0) {
                        onSaveBalance(amount)
                    } else {
                        error = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CaribbeanGreen
                )
            ) {
                Text("Сохранить баланс", color = Color.White)
            }
        }
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
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val predefinedCategories = listOf(
        "Еда" to R.drawable.ic_food,
        "Транспорт" to R.drawable.ic_transport,
        "Развлечения" to R.drawable.ic_entertainment,
        "Жилье" to R.drawable.ic_eye_pass,
        "Здоровье" to R.drawable.ic_eye_pass,
        "Одежда" to R.drawable.ic_eye_pass,
        "Образование" to R.drawable.ic_eye_pass
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color.White,
        modifier = Modifier.fillMaxHeight(0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Заголовок
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Новая трата",
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Закрыть")
                }
            }

            // Быстрый выбор категорий
            Text(
                text = "Выберите категорию:",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(predefinedCategories) { (name, icon) ->
                    val isSelected = selectedCategory == name
                    Card(
                        onClick = {
                            selectedCategory = name
                            categoryName = name
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) CaribbeanGreen else Color.LightGray.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = name,
                                tint = if (isSelected) Color.White else Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }
            }

            // Или введите свою категорию
            OutlinedTextField(
                value = categoryName,
                onValueChange = {
                    categoryName = it
                    if (it !in predefinedCategories.map { it.first }) {
                        selectedCategory = null
                    }
                },
                label = { Text("Название категории") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Или введите свою категорию") }
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
                placeholder = { Text("0.00") },
                leadingIcon = {
                    Text(
                        text = "₽",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            )

            // Кнопка добавления
            PrimaryButton(
                text = "Добавить трату",
                onClick = {
                    if (categoryName.isNotBlank() && amount.isNotBlank() && amount.toFloatOrNull() != null) {
                        onAddExpense(categoryName, amount.toFloat())
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = categoryName.isNotBlank() && amount.isNotBlank() && amount.toFloatOrNull() != null && amount.toFloat() > 0
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
                style = MaterialTheme.typography.bodySmall,
                color = CaribbeanGreen
            )
            Text(
                text = centerSubtext,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
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
                    // Внешняя белая обводка
                    drawArc(
                        color = Color.White,
                        startAngle = startAngle,
                        sweepAngle = adjustedSweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth.toPx() + borderWidth.toPx())
                    )

                    // Основной цвет сегмента
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

                // Разделитель между сегментами
                drawArc(
                    color = Color.White,
                    startAngle = startAngle + adjustedSweep,
                    sweepAngle = gapWidthDegrees,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth.toPx() + borderWidth.toPx())
                )

                startAngle += sweepAngle
            }
        }
    }
}

private fun getCurrentPeriod(): String {
    val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
    val calendar = Calendar.getInstance()
    val currentDate = calendar.time

    // Начало месяца
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startOfMonth = calendar.time

    return "${dateFormat.format(startOfMonth)} — ${dateFormat.format(currentDate)}"
}
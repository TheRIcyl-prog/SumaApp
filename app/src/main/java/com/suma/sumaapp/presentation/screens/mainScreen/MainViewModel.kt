package com.suma.sumaapp.presentation.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import androidx.compose.ui.graphics.Color

data class CircleSegment(
    val color: Color,
    val value: Float,
    val label: String
)

data class MainScreenState(
    val categories: List<CircleSegment> = emptyList(),
    val showAddExpenseSheet: Boolean = false,
    val totalExpenses: Float = 0f
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState

    private val availableColors = listOf(
        Color(0xFFFF6B6B),      // Красный
        Color(0xFF4ECDC4),      // Бирюзовый
        Color(0xFFFFD166),      // Желтый
        Color(0xFF6A0572),      // Фиолетовый
        CaribbeanGreen,         // Карибский зеленый из темы
        Color(0xFF9575CD),      // Лавандовый
        Color(0xFF4DB6AC),      // Аквамарин
        Color(0xFFFFB74D),      // Оранжевый
        Color(0xFFE57373),      // Светло-красный
        Color(0xFF64B5F6)       // Светло-синий
    )

    init {
        // Можно загрузить тестовые данные при инициализации
        loadSampleData()
    }

    fun showAddExpenseSheet() {
        _uiState.value = _uiState.value.copy(showAddExpenseSheet = true)
    }

    fun hideAddExpenseSheet() {
        _uiState.value = _uiState.value.copy(showAddExpenseSheet = false)
    }

    fun addExpense(categoryName: String, amount: Float) {
        viewModelScope.launch {
            // Проверка на валидность данных
            if (categoryName.isBlank() || amount <= 0) {
                return@launch
            }

            val randomColor = availableColors.random()
            val newCategory = CircleSegment(
                color = randomColor,
                value = amount,
                label = categoryName
            )

            val updatedCategories = _uiState.value.categories + newCategory
            val total = updatedCategories.sumOf { it.value.toDouble() }.toFloat()

            _uiState.value = _uiState.value.copy(
                categories = updatedCategories,
                totalExpenses = total
            )

            // После добавления скрываем диалог
            hideAddExpenseSheet()
        }
    }

    fun removeExpense(index: Int) {
        if (index < 0 || index >= _uiState.value.categories.size) {
            return
        }

        val updatedCategories = _uiState.value.categories.toMutableList()
        updatedCategories.removeAt(index)
        val total = updatedCategories.sumOf { it.value.toDouble() }.toFloat()

        _uiState.value = _uiState.value.copy(
            categories = updatedCategories,
            totalExpenses = total
        )
    }

    fun clearAllExpenses() {
        _uiState.value = _uiState.value.copy(
            categories = emptyList(),
            totalExpenses = 0f
        )
    }

    fun loadSampleData() {
        viewModelScope.launch {
            val sampleCategories = listOf(
                CircleSegment(Color(0xFFFF6B6B), 350f, "Продукты"),
                CircleSegment(Color(0xFF4ECDC4), 200f, "Транспорт"),
                CircleSegment(Color(0xFFFFD166), 150f, "Развлечения"),
                CircleSegment(Color(0xFF6A0572), 500f, "Аренда"),
                CircleSegment(CaribbeanGreen, 100f, "Связь")
            )

            val total = sampleCategories.sumOf { it.value.toDouble() }.toFloat()

            _uiState.value = _uiState.value.copy(
                categories = sampleCategories,
                totalExpenses = total
            )
        }
    }
}
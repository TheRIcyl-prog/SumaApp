package com.suma.sumaapp.presentation.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.suma.sumaapp.ui.theme.CaribbeanGreen
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

data class CircleSegment(
    val color: Color,
    val value: Float,
    val label: String,
    val id: String = "",
    val iconResId: Int = android.R.drawable.ic_menu_report_image,
    val percent: Float = 0f,
    val date: String = ""
)

data class MainScreenState(
    val categories: List<CircleSegment> = emptyList(),
    val showAddExpenseSheet: Boolean = false,
    val showBalanceDialog: Boolean = false,
    val totalExpenses: Float = 0f,
    val balance: Float = 0f, // Начальный баланс по умолчанию
    val remainingBalance: Float = 0f,
    val isLoading: Boolean = true,
    val userName: String = "Пользователь",
    val currentDate: String = ""
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState

    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference
    private val userId = auth.currentUser?.uid ?: "test_user" // Для теста используем test_user если нет авторизации

    private val availableColors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFFFFD166),
        Color(0xFF06D6A0),
        CaribbeanGreen,
        Color(0xFF118AB2),
        Color(0xFFEF476F),
        Color(0xFF073B4C),
        Color(0xFFFF9A76),
        Color(0xFF9B5DE5)
    )

    private val categoryIcons = mapOf(
        "Еда" to android.R.drawable.ic_menu_agenda,
        "Транспорт" to android.R.drawable.ic_menu_directions,
        "Развлечения" to android.R.drawable.ic_menu_gallery,
        "Жилье" to android.R.drawable.ic_menu_add,
        "Здоровье" to android.R.drawable.ic_menu_myplaces,
        "Одежда" to android.R.drawable.ic_menu_edit,
        "Образование" to android.R.drawable.ic_menu_info_details,
        "Кафе" to android.R.drawable.ic_menu_my_calendar,
        "Другое" to android.R.drawable.ic_menu_more
    )

    init {
        loadUserData()
        startRealtimeUpdates()
        println("✅ ViewModel инициализирован, userId: $userId")
        println("✅ База данных: ${database.key}")
    }

    private fun startRealtimeUpdates() {
        if (userId.isEmpty()) {
            println("❌ userId пустой, пропускаем realtime updates")
            return
        }

        // Слушаем изменения баланса
        database.child("users").child(userId).child("balance")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val balance = snapshot.getValue(Float::class.java) ?: 10000f
                    println("💰 Баланс обновлен: $balance")

                    viewModelScope.launch {
                        val totalExpenses = _uiState.value.totalExpenses
                        val remaining = max(0f, balance - totalExpenses)

                        _uiState.value = _uiState.value.copy(
                            balance = balance,
                            remainingBalance = remaining
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("❌ Ошибка обновления баланса: ${error.message}")
                }
            })

        // Слушаем изменения расходов
        database.child("users").child(userId).child("expenses")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    println("📊 Данные расходов получены, детей: ${snapshot.childrenCount}")

                    viewModelScope.launch {
                        try {
                            val expenses = mutableListOf<CircleSegment>()
                            var total = 0f

                            snapshot.children.forEach { child ->
                                try {
                                    val expenseId = child.key ?: "unknown"
                                    val category = child.child("category").getValue(String::class.java) ?: "Другое"
                                    val amount = child.child("amount").getValue(Float::class.java) ?: 0f
                                    val date = child.child("date").getValue(String::class.java) ?: ""

                                    println("📝 Расход: $category - $amount ₽")

                                    val iconResId = categoryIcons[category] ?: android.R.drawable.ic_menu_report_image

                                    val colorIndex = when (category) {
                                        "Еда" -> 0
                                        "Транспорт" -> 1
                                        "Развлечения" -> 2
                                        "Жилье" -> 3
                                        "Здоровье" -> 4
                                        "Одежда" -> 5
                                        "Образование" -> 6
                                        "Кафе" -> 7
                                        else -> 8
                                    }

                                    val color = availableColors.getOrElse(colorIndex) { CaribbeanGreen }

                                    expenses.add(
                                        CircleSegment(
                                            id = expenseId,
                                            color = color,
                                            value = amount,
                                            label = category,
                                            iconResId = iconResId,
                                            date = date
                                        )
                                    )
                                    total += amount
                                } catch (e: Exception) {
                                    println("❌ Ошибка парсинга расхода: ${e.message}")
                                }
                            }

                            val sortedExpenses = expenses.sortedByDescending { it.value }
                            val expensesWithPercent = sortedExpenses.map { expense ->
                                val percent = if (total > 0) (expense.value / total) * 100 else 0f
                                expense.copy(percent = percent)
                            }

                            val currentBalance = _uiState.value.balance
                            val remaining = max(0f, currentBalance - total)

                            _uiState.value = _uiState.value.copy(
                                categories = expensesWithPercent,
                                totalExpenses = total,
                                remainingBalance = remaining,
                                isLoading = false,
                                currentDate = getCurrentDateFormatted()
                            )

                            println("✅ Данные обновлены: ${expenses.size} расходов, сумма: $total ₽")
                        } catch (e: Exception) {
                            println("❌ Ошибка обработки: ${e.message}")
                            _uiState.value = _uiState.value.copy(isLoading = false)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("❌ Ошибка получения расходов: ${error.message}")
                    viewModelScope.launch {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            })
    }

    fun loadUserData() {
        if (userId.isEmpty()) {
            println("❌ userId пустой")
            _uiState.value = _uiState.value.copy(isLoading = false)
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        // Создаем пользователя если не существует
        database.child("users").child(userId).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        // Создаем нового пользователя
                        val userData = hashMapOf<String, Any>(
                            "name" to "Новый пользователь",
                            "balance" to 10000f
                        )
                        database.child("users").child(userId).setValue(userData)
                            .addOnSuccessListener {
                                println("✅ Пользователь создан: $userId")
                                viewModelScope.launch {
                                    _uiState.value = _uiState.value.copy(
                                        userName = "Новый пользователь",
                                        balance = 10000f,
                                        isLoading = false
                                    )
                                }
                            }
                    } else {
                        val name = snapshot.getValue(String::class.java) ?: "Пользователь"
                        println("✅ Пользователь загружен: $name")

                        viewModelScope.launch {
                            _uiState.value = _uiState.value.copy(userName = name, isLoading = false)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("❌ Ошибка загрузки пользователя: ${error.message}")
                    viewModelScope.launch {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            })
    }

    private fun getCurrentDateFormatted(): String {
        val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun showAddExpenseSheet() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showAddExpenseSheet = true)
        }
    }

    fun hideAddExpenseSheet() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showAddExpenseSheet = false)
        }
    }

    fun showBalanceDialog() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showBalanceDialog = true)
        }
    }

    fun hideBalanceDialog() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showBalanceDialog = false)
        }
    }

    fun updateBalance(newBalance: Float) {
        if (userId.isEmpty() || newBalance < 0) return

        viewModelScope.launch {
            try {
                database.child("users").child(userId).child("balance")
                    .setValue(newBalance)
                    .addOnSuccessListener {
                        println("✅ Баланс обновлен: $newBalance ₽")
                    }
                    .addOnFailureListener { e ->
                        println("❌ Ошибка обновления баланса: ${e.message}")
                    }

                hideBalanceDialog()
            } catch (e: Exception) {
                println("❌ Ошибка: ${e.message}")
            }
        }
    }

    // Упрощенное добавление расхода - используем простую Map
    fun addExpense(categoryName: String, amount: Float) {
        if (userId.isEmpty() || categoryName.isBlank() || amount <= 0) {
            println("❌ Некорректные данные: category=$categoryName, amount=$amount")
            return
        }

        viewModelScope.launch {
            try {
                val expenseId = database.child("users").child(userId).child("expenses").push().key
                    ?: System.currentTimeMillis().toString()

                val expenseData = hashMapOf<String, Any>(
                    "category" to categoryName,
                    "amount" to amount,
                    "date" to SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()),
                    "timestamp" to System.currentTimeMillis()
                )

                println("➕ Добавляем расход: $categoryName - $amount ₽")

                database.child("users").child(userId).child("expenses").child(expenseId)
                    .setValue(expenseData)
                    .addOnSuccessListener {
                        println("✅ Расход сохранен в Firebase!")
                        println("   ID: $expenseId")
                        println("   Категория: $categoryName")
                        println("   Сумма: $amount ₽")

                        hideAddExpenseSheet()

                        // Обновляем текущую дату
                        _uiState.value = _uiState.value.copy(
                            currentDate = getCurrentDateFormatted()
                        )
                    }
                    .addOnFailureListener { e ->
                        println("❌ Ошибка сохранения: ${e.message}")
                    }
            } catch (e: Exception) {
                println("❌ Ошибка: ${e.message}")
            }
        }
    }

    // Упрощенное удаление расхода
    fun removeExpense(expenseId: String) {
        if (userId.isEmpty() || expenseId.isEmpty()) return

        viewModelScope.launch {
            try {
                database.child("users").child(userId).child("expenses").child(expenseId)
                    .removeValue()
                    .addOnSuccessListener {
                        println("✅ Расход удален: $expenseId")
                    }
                    .addOnFailureListener { e ->
                        println("❌ Ошибка удаления: ${e.message}")
                    }
            } catch (e: Exception) {
                println("❌ Ошибка: ${e.message}")
            }
        }
    }

    // Упрощенная очистка всех расходов
    fun clearAllExpenses() {
        if (userId.isEmpty()) return

        viewModelScope.launch {
            try {
                database.child("users").child(userId).child("expenses")
                    .removeValue()
                    .addOnSuccessListener {
                        println("✅ Все расходы очищены")
                    }
                    .addOnFailureListener { e ->
                        println("❌ Ошибка очистки: ${e.message}")
                    }
            } catch (e: Exception) {
                println("❌ Ошибка: ${e.message}")
            }
        }
    }
}
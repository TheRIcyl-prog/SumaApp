package com.suma.sumaapp.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import android.util.Patterns
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val registrationDate: String = "",
    val profileImageUrl: String? = null
) {
    constructor() : this("", "", "", "", null)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "registrationDate" to registrationDate,
            "profileImageUrl" to profileImageUrl
        )
    }
}

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val registrationSuccess: Boolean = false
)

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val database: DatabaseReference = Firebase.database.reference
    private val usersRef: DatabaseReference = database.child("users")

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Валидация
                val name = _uiState.value.name
                val email = _uiState.value.email
                val password = _uiState.value.password
                val confirmPassword = _uiState.value.confirmPassword

                if (name.isBlank()) {
                    throw Exception("Введите имя")
                }

                if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    throw Exception("Введите корректный email")
                }

                if (password.isBlank() || password.length < 6) {
                    throw Exception("Пароль должен содержать минимум 6 символов")
                }

                if (password != confirmPassword) {
                    throw Exception("Пароли не совпадают")
                }

                // Создание пользователя в Firebase Auth
                println("Создание пользователя в Firebase Auth...")
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user ?: throw Exception("Не удалось создать пользователя")

                println("Пользователь создан в Auth, UID: ${user.uid}")

                // Создание объекта пользователя
                val newUser = User(
                    id = user.uid,
                    name = name,
                    email = email,
                    registrationDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date()),
                    profileImageUrl = null
                )

                // Сохранение в Realtime Database
                saveUserToDatabase(newUser)

                // Успешная регистрация
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    registrationSuccess = true
                )
                onSuccess()

            } catch (e: Exception) {
                println("Ошибка регистрации: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка регистрации"
                )
                onError(e.message ?: "Ошибка регистрации")
            }
        }
    }

    private suspend fun saveUserToDatabase(user: User) {
        try {
            println("Сохранение пользователя в Realtime Database...")
            println("Путь: users/${user.id}")
            println("Данные: ${user.toMap()}")

            // Используем await() для ожидания завершения
            usersRef.child(user.id).setValue(user.toMap()).await()

            println("✅ Пользователь успешно сохранен в Firebase Realtime Database")

        } catch (e: Exception) {
            println("❌ Ошибка сохранения пользователя: ${e.message}")
            throw e // Пробрасываем ошибку дальше
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
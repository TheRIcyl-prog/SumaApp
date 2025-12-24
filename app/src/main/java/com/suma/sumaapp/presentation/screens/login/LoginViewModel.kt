package com.suma.sumaapp.presentation.screens.login

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import com.google.firebase.database.database
import com.google.firebase.database.getValue

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Состояние для экрана входа
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false,
    val userData: UserData? = null
)

// Данные пользователя
data class UserData(
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

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database.reference

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    // Список для хранения последних email
    private val recentEmails = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
        // Добавляем email в историю
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && !recentEmails.contains(email)) {
            if (recentEmails.size >= 5) recentEmails.removeFirst()
            recentEmails.add(email)
        }
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun getRecentEmails(): List<String> {
        return recentEmails.reversed()
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (UserData) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Валидация
                if (email.isBlank()) {
                    throw Exception("Введите email")
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    throw Exception("Введите корректный email")
                }

                if (password.isBlank()) {
                    throw Exception("Введите пароль")
                }

                if (password.length < 6) {
                    throw Exception("Пароль должен содержать минимум 6 символов")
                }

                // Вход в Firebase Auth
                println("Вход в Firebase Auth...")
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user ?: throw Exception("Не удалось войти в аккаунт")

                println("Успешный вход, UID: ${user.uid}")

                // Получение данных пользователя из базы
                val userData = getUserDataFromDatabase(user.uid)

                // Обновление состояния
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    userData = userData
                )

                onSuccess(userData)

            } catch (e: Exception) {
                println("Ошибка входа: ${e.message}")
                val errorMessage = when {
                    e.message?.contains("password is invalid") == true ->
                        "Неверный пароль"
                    e.message?.contains("no user record") == true ->
                        "Пользователь с таким email не найден"
                    e.message?.contains("network error") == true ->
                        "Ошибка сети. Проверьте подключение к интернету"
                    e.message?.contains("blocked all requests") == true ->
                        "Слишком много попыток входа. Попробуйте позже"
                    e.message?.contains("badly formatted") == true ->
                        "Некорректный формат email"
                    else -> e.message ?: "Ошибка входа. Попробуйте еще раз"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                onError(errorMessage)
            }
        }
    }

    private suspend fun getUserDataFromDatabase(userId: String): UserData {
        return try {
            println("Получение данных пользователя из базы...")
            println("Путь: users/$userId")

            val snapshot = database.child("users").child(userId).get().await()

            if (snapshot.exists()) {
                val userData = snapshot.getValue<UserData>() ?: UserData(
                    id = userId,
                    email = _uiState.value.email
                )
                println("Данные пользователя получены: $userData")
                userData
            } else {
                println("Данные пользователя не найдены в базе")
                UserData(
                    id = userId,
                    email = _uiState.value.email
                )
            }
        } catch (e: Exception) {
            println("Ошибка получения данных пользователя: ${e.message}")
            UserData(
                id = userId,
                email = _uiState.value.email
            )
        } as UserData
    }

    // Автовход
    fun checkAutoLogin(onUserLoggedIn: (UserData) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    println("Пользователь уже авторизован, UID: ${currentUser.uid}")
                    val userData = getUserDataFromDatabase(currentUser.uid)
                    _uiState.value = _uiState.value.copy(
                        userData = userData,
                        loginSuccess = true
                    )
                    onUserLoggedIn(userData)
                } catch (e: Exception) {
                    println("Ошибка при автовходе: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // Выход из аккаунта
    fun logout() {
        auth.signOut()
        _uiState.value = LoginState()
        println("Пользователь вышел из системы")
    }

    // Проверка авторизации
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Получение текущего пользователя
    fun getCurrentUser(): UserData? {
        return auth.currentUser?.let { user ->
            UserData(
                id = user.uid,
                name = user.displayName ?: "",
                email = user.email ?: ""
            )
        }
    }

    // Восстановление пароля
    fun resetPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    throw Exception("Введите корректный email")
                }

                auth.sendPasswordResetEmail(email).await()
                onSuccess()
                println("Письмо для сброса пароля отправлено на $email")

            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("user not found") == true ->
                        "Пользователь с таким email не найден"
                    else -> "Ошибка при отправке письма: ${e.message}"
                }
                onError(errorMessage)
            }
        }
    }
}
package com.suma.sumaapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Здесь должна быть логика аутентификации
            // Например, вызов API

            try {
                // Имитация задержки сети
                kotlinx.coroutines.delay(1000)

                // Проверка данных
                val email = _uiState.value.email
                val password = _uiState.value.password

                if (email.isBlank() || password.isBlank()) {
                    throw Exception("Заполните все поля")
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    throw Exception("Неверный формат email")
                }

                // Успешный вход
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка входа"
                )
                onError(e.message ?: "Ошибка входа")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
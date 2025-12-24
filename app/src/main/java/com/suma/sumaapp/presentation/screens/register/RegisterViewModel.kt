package com.suma.sumaapp.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {

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

                if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    throw Exception("Введите корректный email")
                }

                if (password.isBlank() || password.length < 6) {
                    throw Exception("Пароль должен содержать минимум 6 символов")
                }

                if (password != confirmPassword) {
                    throw Exception("Пароли не совпадают")
                }

                // Имитация регистрации
                kotlinx.coroutines.delay(1500)

                // Успешная регистрация
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка регистрации"
                )
                onError(e.message ?: "Ошибка регистрации")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
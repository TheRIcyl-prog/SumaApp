package com.suma.sumaapp.presentation.screens.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchViewModel : ViewModel() {

    // Простое состояние загрузки (опционально)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Запуск автоматической навигации
    fun startAutoNavigation(navigateToLogin: () -> Unit) {
        viewModelScope.launch {
            delay(3000) // 3 секунды задержки
            navigateToLogin()
        }
    }

    // Обработка нажатия кнопки входа
    fun onLoginClick(navigateToLogin: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(50) // Короткая задержка для UX
            navigateToLogin()
            _isLoading.value = false
        }
    }

    // Обработка нажатия кнопки регистрации
    fun onRegisterClick(navigateToRegister: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(5000)
            navigateToRegister()
            _isLoading.value = false
        }
    }

    // Обработка "Забыли пароль?"
    fun onForgotPasswordClick(navigateToPasswordRecovery: () -> Unit) {
        navigateToPasswordRecovery()
    }
}
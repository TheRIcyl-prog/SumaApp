package com.suma.sumaapp.presentation.screens.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Автоматическая навигация после задержки (например, если уже авторизован)
    fun startAutoNavigation(navigateToMain: () -> Unit) {
        viewModelScope.launch {
            delay(2000) // 2 секунды задержки
            navigateToMain()
        }
    }

    fun onLoginClick(navigateToLogin: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(500) // Короткая задержка для UX
            navigateToLogin()
            _isLoading.value = false
        }
    }

    fun onRegisterClick(navigateToRegister: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(500) // Исправлено: было 5000 (слишком много)
            navigateToRegister()
            _isLoading.value = false
        }
    }

    fun onForgotPasswordClick(navigateToPasswordRecovery: () -> Unit) {
        navigateToPasswordRecovery()
    }
}
package com.example.smartcoworking.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcoworking.data.models.Usuario
import com.example.smartcoworking.utils.SecurityUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _senha = MutableStateFlow("")
    val senha: StateFlow<String> = _senha.asStateFlow()

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Mock user for testing
    private val mockUser = Usuario(
        id = "1",
        nome = "Lucas Fonseca",
        email = "lucas@smartcoworking.com",
        senhaHash = SecurityUtils.hashSenha("123456") // Password is "123456"
    )

    private val demoUser = Usuario(
        id = "2",
        nome = "Usuário Demo",
        email = "demo@smartcoworking.com",
        senhaHash = SecurityUtils.hashSenha("demo123")
    )

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }

    fun onSenhaChange(newSenha: String) {
        _senha.value = newSenha
        if (_uiState.value is LoginUiState.Error) {
            _uiState.value = LoginUiState.Idle
        }
    }

    fun fillDemoCredentials() {
        onEmailChange(demoUser.email)
        onSenhaChange("demo123")
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(1500) // Simulate network delay

            if (!isValidEmail(_email.value)) {
                _uiState.value = LoginUiState.Error("Formato de email inválido")
                return@launch
            }

            if (!isValidPassword(_senha.value)) {
                _uiState.value = LoginUiState.Error("A senha deve ter pelo menos 6 caracteres")
                return@launch
            }

            val user = if (_email.value == mockUser.email) mockUser else if (_email.value == demoUser.email) demoUser else null
            
            if (user != null && SecurityUtils.verificarSenha(_senha.value, user.senhaHash)) {
                _uiState.value = LoginUiState.Success
            } else {
                _uiState.value = LoginUiState.Error("Email ou senha incorretos")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

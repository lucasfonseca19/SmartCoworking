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

    fun login() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            delay(1500) // Simulate network delay

            val emailValido = _email.value == mockUser.email
            val senhaValida = SecurityUtils.verificarSenha(_senha.value, mockUser.senhaHash)

            if (emailValido && senhaValida) {
                _uiState.value = LoginUiState.Success
            } else {
                _uiState.value = LoginUiState.Error("Email ou senha incorretos")
            }
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

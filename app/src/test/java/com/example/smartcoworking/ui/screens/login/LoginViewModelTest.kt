package com.example.smartcoworking.ui.screens.login

import com.example.smartcoworking.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with correct credentials updates state to Success`() = runTest {
        val viewModel = LoginViewModel()
        
        viewModel.onEmailChange("lucas@smartcoworking.com")
        viewModel.onSenhaChange("123456")
        
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertTrue(viewModel.uiState.value is LoginUiState.Success)
    }

    @Test
    fun `login with incorrect credentials updates state to Error`() = runTest {
        val viewModel = LoginViewModel()
        
        viewModel.onEmailChange("lucas@smartcoworking.com") // Valid email
        viewModel.onSenhaChange("wrongpass") // Valid length, wrong pass
        
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertTrue(viewModel.uiState.value is LoginUiState.Error)
        assertEquals("Email ou senha incorretos", (viewModel.uiState.value as LoginUiState.Error).message)
    }

    @Test
    fun `login with invalid email format updates state to Error`() = runTest {
        val viewModel = LoginViewModel()

        viewModel.onEmailChange("invalid-email")
        viewModel.onSenhaChange("123456")

        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoginUiState.Error)
        assertEquals("Formato de email inválido", (viewModel.uiState.value as LoginUiState.Error).message)
    }

    @Test
    fun `login with short password updates state to Error`() = runTest {
        val viewModel = LoginViewModel()

        viewModel.onEmailChange("lucas@smartcoworking.com")
        viewModel.onSenhaChange("123")

        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is LoginUiState.Error)
        assertEquals("A senha deve ter pelo menos 6 caracteres", (viewModel.uiState.value as LoginUiState.Error).message)
    }
}

package com.example.pelucita.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pelucita.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            val emailError = validateEmail(email)
            currentState.copy(
                email = email,
                emailError = emailError,
                isLoginEnabled = isFormValid(
                    email, currentState.password,
                    emailError, currentState.passwordError
                )
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            val passwordError = validatePassword(password)
            currentState.copy(
                password = password,
                passwordError = passwordError,
                isLoginEnabled = isFormValid(
                    currentState.email, password,
                    currentState.emailError, passwordError
                )
            )
        }
    }

    private fun validateEmail(email: String): Int? {
        return when {
            email.isEmpty() -> null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_invalid_email
            else -> null
        }
    }

    private fun validatePassword(password: String): Int? {
        return when {
            password.isEmpty() -> null
            password.length < 8 -> R.string.error_password_short
            password.none { it.isDigit() } -> R.string.error_password_number
            else -> null
        }
    }

    private fun isFormValid(
        email: String, password: String,
        emailError: Int?, passwordError: Int?
    ): Boolean {
        return email.isNotBlank() &&
                password.isNotBlank() &&
                emailError == null &&
                passwordError == null
    }

    fun onLoginClicked() {
        val currentState = _uiState.value
        if (!currentState.isLoginEnabled || currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val isSuccess = simulateApiCall(currentState.email, currentState.password)

            _uiState.update { it.copy(isLoading = false) }

            if (isSuccess) {
                _uiEvent.send(LoginUiEvent.NavigateToHome)
            } else {
                _uiEvent.send(LoginUiEvent.ShowError(R.string.error_wrong_credentials))
            }
        }
    }

    private suspend fun simulateApiCall(email: String, password: String): Boolean {
        delay(1500)
        return email == "admin@test.com" && password == "Admin123"
    }
}
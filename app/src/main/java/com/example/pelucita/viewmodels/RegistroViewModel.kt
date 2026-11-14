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

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<RegistroUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onNameChanged(name: String) {
        _uiState.update { currentState ->
            val nameError = validateName(name)
            currentState.copy(
                name = name,
                nameError = nameError,
                isRegisterEnabled = isFormValid(
                    name, nameError,
                    currentState.email, currentState.emailError,
                    currentState.password, currentState.passwordError
                )
            )
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            val emailError = validateEmail(email)
            currentState.copy(
                email = email,
                emailError = emailError,
                isRegisterEnabled = isFormValid(
                    currentState.name, currentState.nameError,
                    email, emailError,
                    currentState.password, currentState.passwordError
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
                isRegisterEnabled = isFormValid(
                    currentState.name, currentState.nameError,
                    currentState.email, currentState.emailError,
                    password, passwordError
                )
            )
        }
    }

    private fun validateName(name: String): Int? {
        return when {
            name.isEmpty() -> null
            name.trim().length < 3 -> R.string.error_name_short
            else -> null
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
        name: String, nameError: Int?,
        email: String, emailError: Int?,
        password: String, passwordError: Int?
    ): Boolean {
        return name.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                nameError == null &&
                emailError == null &&
                passwordError == null
    }

    fun onRegisterClicked() {
        val currentState = _uiState.value
        if (!currentState.isRegisterEnabled) return

        viewModelScope.launch {
            delay(1500)
            _uiEvent.send(RegistroUiEvent.NavigateToSignIn)
        }
    }
}
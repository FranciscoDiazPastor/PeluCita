package com.example.pelucita.viewmodels

data class RegistroUiState(
    val name: String = "",
    val nameError: Int? = null,
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val isRegisterEnabled: Boolean = false
)
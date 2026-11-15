package com.example.pelucita.viewmodels

data class RecoveryUiState(
    val email: String = "",
    val emailError: Int? = null,
    val isRecoveryEnabled: Boolean = false
)
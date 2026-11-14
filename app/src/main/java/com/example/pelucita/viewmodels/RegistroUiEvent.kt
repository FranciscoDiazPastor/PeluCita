package com.example.pelucita.viewmodels

sealed interface RegistroUiEvent {
    object NavigateToSignIn : RegistroUiEvent
    data class ShowError(val messageResId: Int) : RegistroUiEvent
}
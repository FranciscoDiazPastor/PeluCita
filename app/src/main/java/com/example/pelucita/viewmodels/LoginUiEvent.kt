package com.example.pelucita.viewmodels

sealed interface LoginUiEvent
{
    object NavigateToHome : LoginUiEvent
    data class ShowError(val messageResId: Int) : LoginUiEvent
}
package com.example.pelucita.viewmodels

sealed interface RecoveryUiEvent {
    object CodeSent : RecoveryUiEvent
}
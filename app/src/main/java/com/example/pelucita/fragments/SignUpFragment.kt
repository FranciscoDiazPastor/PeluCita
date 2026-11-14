package com.example.pelucita.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.pelucita.R
import com.example.pelucita.databinding.FragmentSignUpBinding
import com.example.pelucita.viewmodels.RegistroUiEvent
import com.example.pelucita.viewmodels.RegistroViewModel
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // El registro es la única pantalla con vertical y horizontal
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cada letra escrita o borrada avisa al ViewModel
        binding.etName.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
        }
        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.btnSignUp.setOnClickListener {
            viewModel.onRegisterClicked()
        }

        // Observar el estado y los eventos
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Escuchar cambios de estado de la UI
                launch {
                    viewModel.uiState.collect { state ->
                        // Recuperar lo escrito tras un giro de pantalla.
                        // El if evita el bucle setText -> doOnTextChanged -> estado
                        if (binding.etName.text.toString() != state.name) {
                            binding.etName.setText(state.name)
                        }
                        if (binding.etEmail.text.toString() != state.email) {
                            binding.etEmail.setText(state.email)
                        }
                        if (binding.etPassword.text.toString() != state.password) {
                            binding.etPassword.setText(state.password)
                        }

                        // Errores bajo los campos
                        binding.tilName.error = state.nameError?.let { getString(it) }
                        binding.tilEmail.error = state.emailError?.let { getString(it) }
                        binding.tilPassword.error = state.passwordError?.let { getString(it) }

                        // Habilitar / deshabilitar el botón
                        binding.btnSignUp.isEnabled = state.isRegisterEnabled
                    }
                }

                // Escuchar eventos de un solo uso
                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is RegistroUiEvent.NavigateToSignIn -> {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.msg_account_created),
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(
                                    R.id.action_signUpFragment_to_signInFragment
                                )
                            }

                            is RegistroUiEvent.ShowError -> {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(event.messageResId),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        // Enlace a Login que ya existía en la Tarea 1
        binding.tvGoSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }
}
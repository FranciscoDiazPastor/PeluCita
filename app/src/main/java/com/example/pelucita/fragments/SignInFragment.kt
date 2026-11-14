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
import com.example.pelucita.databinding.FragmentSignInBinding
import com.example.pelucita.viewmodels.LoginUiEvent
import com.example.pelucita.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.btnSignIn.setOnClickListener {
            viewModel.onLoginClicked()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiState.collect { state ->
                        binding.tilEmail.error = state.emailError?.let { getString(it) }
                        binding.tilPassword.error = state.passwordError?.let { getString(it) }

                        binding.btnSignIn.isEnabled = state.isLoginEnabled && !state.isLoading
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is LoginUiEvent.NavigateToHome ->
                                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)

                            is LoginUiEvent.ShowError ->
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

        binding.tvForgot.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_recoveryFragment)
        }

        binding.tvGoSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }
}
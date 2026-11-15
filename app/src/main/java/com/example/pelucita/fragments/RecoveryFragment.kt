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
import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.pelucita.R
import com.example.pelucita.databinding.FragmentRecoveryBinding
import com.example.pelucita.viewmodels.RecoveryUiEvent
import com.example.pelucita.viewmodels.RecoveryViewModel
import kotlinx.coroutines.launch

class RecoveryFragment : Fragment() {

    private lateinit var binding: FragmentRecoveryBinding
    private val viewModel: RecoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecoveryBinding.inflate(inflater, container, false)
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

        binding.btnSendCode.setOnClickListener {
            viewModel.onSendCodeClicked()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.tilEmail.error = state.emailError?.let { getString(it) }
                        binding.btnSendCode.isEnabled = state.isRecoveryEnabled
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is RecoveryUiEvent.CodeSent -> {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.recovery_code_sent),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.tvBackToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_recoveryFragment_to_signInFragment)
        }
    }
}
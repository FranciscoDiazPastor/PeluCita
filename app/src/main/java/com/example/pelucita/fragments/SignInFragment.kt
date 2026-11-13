package com.example.pelucita.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pelucita.R
import com.example.pelucita.databinding.FragmentSignInBinding
import android.content.pm.ActivityInfo


class SignInFragment : Fragment()
{
    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.tvForgot.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_recoveryFragment)
        }

        binding.tvGoSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
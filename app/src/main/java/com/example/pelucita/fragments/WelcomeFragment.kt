package com.example.pelucita.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pelucita.R
import com.example.pelucita.databinding.FragmentWelcomeBinding
import com.google.android.material.snackbar.Snackbar
class WelcomeFragment : Fragment()
{
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(inflater: LayoutInflater, container:
    ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        binding = FragmentWelcomeBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.btnWelcomeSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }
        binding.btnWelcomeSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signInFragment)
        }
        binding.tvGuest.setOnClickListener {
            Snackbar.make(binding.root, R.string.msg_guest_mode,
                Snackbar.LENGTH_LONG).show()
        }
    }
}

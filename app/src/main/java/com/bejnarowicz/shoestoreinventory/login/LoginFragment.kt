package com.bejnarowicz.shoestoreinventory.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bejnarowicz.shoestoreinventory.R
import com.bejnarowicz.shoestoreinventory.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()


        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }

        binding.loginButton.setOnClickListener {

            if (binding.etEmail.text.isNullOrEmpty() && binding.etPassword.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter the required fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_mainViewFragment)
            }
        }

        return binding.root
    }


}
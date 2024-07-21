package com.example.mybouquetkotlin.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mybouquetkotlin.ViewModel.Fragments.LoginViewModel
import com.example.mybouquetkotlin.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var viewModel:LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.signIn.setOnClickListener {
            //verify data
            viewModel.signIn(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())
        }

        binding.createNewUser.setOnClickListener {
            //verify data
            viewModel.createUser(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())
        }
        viewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if(it != null){
                findNavController().popBackStack()
            }
        })
        return binding.root
    }
}

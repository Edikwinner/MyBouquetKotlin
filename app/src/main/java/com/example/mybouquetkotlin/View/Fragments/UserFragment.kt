package com.example.mybouquetkotlin.View.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.ViewModel.Fragments.UserViewModel
import com.example.mybouquetkotlin.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private lateinit var viewModel:UserViewModel
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater)
        Log.i("TAG", "created")
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.refreshData()
        viewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.currentPhone.text = it.phoneNumber.toString()
                binding.currentEmail.text = viewModel.cardRepository.getCurrentEmail().toString()
            }
            else{
                findNavController().navigate(R.id.loginFragment)
            }
        })

        binding.logOff.setOnClickListener {
            viewModel.logOff()
        }

        binding.savePhoneNumber.setOnClickListener {
            //verify data
            viewModel.savePhoneNumber(binding.inputPhoneNumberUser.text.toString())
            binding.currentPhone.text = binding.inputPhoneNumberUser.text.toString()
            binding.inputPhoneNumberUser.text = null

        }
        return binding.root
    }
}
       /* val email: TextView = rootView.findViewById(R.id.currentEmail)
        phone = rootView.findViewById(R.id.currentPhone)
        inputPhone = rootView.findViewById(R.id.inputPhoneNumberUser)

        email.setText(cards.firebaseAuth!!.getCurrentUser()!!.getEmail())
        phone.setText(cards.phoneNumber)
        rootView.findViewById<Button>(R.id.savePhoneNumber)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if(!inputPhone.text.toString().isEmpty()) {
                        cards.addPhoneNumberToUser(inputPhone.text.toString())
                        inputPhone.setText("")
                        phone.text = cards.phoneNumber.toString()
                    }
                }
            })
        rootView.findViewById<View>(R.id.logOff).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cards.firebaseAuth!!.signOut()
                cards.UID = null
                cards.toShoppingCartCards = ArrayList()
                cards.favouriteCards = ArrayList()
                cards.phoneNumber = null

                val loginFragment: LoginFragment = LoginFragment()
                val loginBundle: Bundle = Bundle()
                loginBundle.putSerializable("cards", cards)
                loginFragment.setArguments(loginBundle)
                requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, loginFragment).commit()
            }
        })*/

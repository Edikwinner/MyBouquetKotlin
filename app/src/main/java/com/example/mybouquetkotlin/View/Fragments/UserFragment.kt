package com.example.mybouquetkotlin.View.Fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.Model.Cards
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.ViewModel.Fragments.UserViewModel

class UserFragment() : Fragment() {
    private lateinit var viewModel:UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_user, container, false)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val logOff = rootView.findViewById<Button>(R.id.logOff)
        val emailTextView = rootView.findViewById<TextView>(R.id.currentEmail)
        val numberTextView = rootView.findViewById<TextView>(R.id.currentPhone)
        val numberEditText = rootView.findViewById<EditText>(R.id.inputPhoneNumberUser)
        val saveNumberButton = rootView.findViewById<Button>(R.id.savePhoneNumber)


        viewModel.mainActivityViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if(it == null){
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment, LoginFragment())
                    .commit()
            }
            else{
                numberTextView.text = it.phoneNumber.toString()
                emailTextView.text = viewModel.cardRepository.getCurrentEmail().toString()
            }
        })



        logOff.setOnClickListener {
            viewModel.logOff()
        }

        saveNumberButton.setOnClickListener {
            //verify data
            viewModel.savePhoneNumber(numberEditText.text.toString())
            numberTextView.text = numberEditText.text.toString()
            numberEditText.text = null

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
        return rootView
    }
}
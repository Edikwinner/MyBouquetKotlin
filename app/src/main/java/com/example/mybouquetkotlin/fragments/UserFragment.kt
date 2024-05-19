package com.example.mybouquetkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.data.Cards

class UserFragment() : Fragment() {
    var cards: Cards? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            cards = getArguments()!!.get("cards") as Cards?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_user, container, false)
        val email: TextView = rootView.findViewById(R.id.currentEmail)
        val phone: TextView = rootView.findViewById(R.id.currentPhone)
        val inputPhone: EditText = rootView.findViewById(R.id.inputPhoneNumberUser)

        email.setText(cards!!.firebaseAuth!!.getCurrentUser()!!.getEmail())
        phone.setText(cards!!.phoneNumber)
        rootView.findViewById<View>(R.id.savePhoneNumber)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    cards!!.addPhoneNumberToUser(inputPhone.getText().toString())
                    inputPhone.setText("")
                }
            })
        rootView.findViewById<View>(R.id.logOff).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cards!!.firebaseAuth!!.signOut()
                cards!!.UID = null
                cards!!.toShoppingCartCards = ArrayList()
                cards!!.favouriteCards = ArrayList()
                cards!!.phoneNumber = null

                val loginFragment: LoginFragment = LoginFragment()
                val loginBundle: Bundle = Bundle()
                loginBundle.putSerializable("cards", cards)
                loginFragment.setArguments(loginBundle)
                getActivity()!!.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, loginFragment).commit()
            }
        })
        return rootView
    }
}
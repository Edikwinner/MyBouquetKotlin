package com.example.mybouquetkotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.data.Card
import com.example.mybouquetkotlin.data.Cards
import com.example.mybouquetkotlin.data.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class LoginFragment() : Fragment() {
    private var cards: Cards? = null


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
        val RootView: View = inflater.inflate(R.layout.fragment_login, container, false)

        val email: EditText = RootView.findViewById(R.id.input_email)
        val password: EditText = RootView.findViewById(R.id.input_password)
        RootView
            .findViewById<View>(R.id.create_new_user)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    createNewUser(email.getText().toString(), password.getText().toString())
                }
            })

        RootView.findViewById<View>(R.id.sign_in).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                signIn(email.getText().toString(), password.getText().toString())
            }
        })
        return RootView
    }

    private fun createNewUser(email: String, password: String) {
        cards!!.firebaseAuth
            ?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        cards!!.UID = cards!!.firebaseAuth!!.getCurrentUser()!!.getUid()
                        createUserInDatabase()
                        signIn(email, password)
                    } else {
                        Toast.makeText(
                            getContext(),
                            "Введены некорретные данные",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun signIn(email: String, password: String) {
        cards!!.firebaseAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        cards!!.UID = cards!!.firebaseAuth!!.getCurrentUser()!!.getUid()
                        cards!!.firestore!!.collection("users").document(cards!!.UID!!).get()
                            .addOnSuccessListener(object : OnSuccessListener<DocumentSnapshot> {
                                override fun onSuccess(documentSnapshot: DocumentSnapshot) {
                                    val user: User? = documentSnapshot.toObject(
                                        User::class.java
                                    )
                                    cards!!.phoneNumber = user!!.phoneNumber
                                    val favouriteCardsList: List<String?>? =
                                        user!!.favouriteCardsPath
                                    val toShoppingCardList: List<String?>? =
                                        user!!.cardsToShoppingCartPath
                                    for (cardPath: String? in favouriteCardsList!!) {
                                        for (card: Card? in cards!!.cards) {
                                            if ((card!!.path == cardPath)) {
                                                cards!!.favouriteCards.add(card)
                                                Log.i("TAG", "added to fav cards")
                                                break
                                            }
                                        }
                                    }

                                    for (cardPath: String? in toShoppingCardList!!) {
                                        for (card: Card? in cards!!.cards) {
                                            if ((card!!.path == cardPath)) {
                                                cards!!.toShoppingCartCards.add(card)
                                                Log.i("TAG", "added to shop cards")
                                                break
                                            }
                                        }
                                    }
                                    cards!!.ordersList = user.orders

                                    val userFragment: UserFragment = UserFragment()
                                    val userBundle: Bundle = Bundle()
                                    userBundle.putSerializable("cards", cards)
                                    userFragment.setArguments(userBundle)
                                    requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment, userFragment).commit()
                                }
                            })
                        Log.i("TAG", "UID: " + cards!!.UID)
                        Log.i("TAG", "fav: " + cards!!.favouriteCards.size.toString())
                        Log.i("TAG", "shop: " + cards!!.toShoppingCartCards.size.toString())
                    } else {
                        Toast.makeText(
                            getContext(),
                            "Введены некорретные данные",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun createUserInDatabase() {
        cards!!.firestore!!.collection("users").get()
            .addOnSuccessListener(object : OnSuccessListener<QuerySnapshot> {
                override fun onSuccess(queryDocumentSnapshots: QuerySnapshot) {
                    var hasUser: Boolean = false
                    for (document: QueryDocumentSnapshot in queryDocumentSnapshots) {
                        if ((document.getId() == cards!!.UID)) {
                            hasUser = true
                        }
                    }
                    if (!hasUser) {
                        val user: User = User()
                        user.iD = cards!!.UID
                        cards!!.firestore!!.collection("users").document((cards!!.UID)!!).set(user)
                    } else {
                        cards!!.firestore!!.collection("users").document((cards!!.UID)!!).get()
                            .addOnSuccessListener(object : OnSuccessListener<DocumentSnapshot> {
                                override fun onSuccess(documentSnapshot: DocumentSnapshot) {
                                    val user: User? = documentSnapshot.toObject(
                                        User::class.java
                                    )
                                    val favouriteCardsList: List<String?>? =
                                        user!!.favouriteCardsPath
                                    val toShoppingCardList: List<String?>? =
                                        user!!.cardsToShoppingCartPath
                                    for (cardPath: String? in favouriteCardsList!!) {
                                        for (card: Card? in cards!!.cards) {
                                            if ((card!!.path == cardPath)) {
                                                cards!!.favouriteCards.add(card)
                                                break
                                            }
                                        }
                                    }

                                    for (cardPath: String? in toShoppingCardList!!) {
                                        for (card: Card? in cards!!.cards) {
                                            if ((card!!.path == cardPath)) {
                                                cards!!.toShoppingCartCards.add(card)
                                                break
                                            }
                                        }
                                    }
                                }
                            })
                    }
                }
            })
    }
}
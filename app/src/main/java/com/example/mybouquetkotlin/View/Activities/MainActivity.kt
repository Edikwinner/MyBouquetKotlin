package com.example.mybouquetkotlin.View.Activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.mybouquetkotlin.ViewModel.Activities.MainActivityViewModel
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Cards
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.View.Fragments.AddFragment
import com.example.mybouquetkotlin.View.Fragments.FavouriteFragment
import com.example.mybouquetkotlin.View.Fragments.HomeFragment
import com.example.mybouquetkotlin.View.Fragments.LoginFragment
import com.example.mybouquetkotlin.View.Fragments.ShoppingCardFragment
import com.example.mybouquetkotlin.View.Fragments.UserFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage

class MainActivity() : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]


        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment, HomeFragment())
            .commit()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.getItemId() == R.id.home_screen) {
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, HomeFragment())
                        .commit()
                    return true
                } else if (item.getItemId() == R.id.favourite_screen) {
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, FavouriteFragment())
                        .commit()
                    return true
                } else if (item.getItemId() == R.id.custom_bouquet_screen) {
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, AddFragment())
                        .commit()
                    return true
                } else if (item.getItemId() == R.id.shopping_card_screen) {
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, ShoppingCardFragment())
                        .commit()
                    return true
                } else if (item.getItemId() == R.id.profile_screen) {
                    if(viewModel.currentUser.value != null) {
                        getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, UserFragment())
                            .commit()
                    }
                    else{
                        getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, LoginFragment())
                            .commit()
                    }
                    return true
                }
                return false
            }
        })
    }
}


        //setFirestore();
        /*firestore!!.collection("bouquets").get()
            .addOnSuccessListener(object : OnSuccessListener<QuerySnapshot> {
                override fun onSuccess(queryDocumentSnapshots: QuerySnapshot) {
                    for (document: QueryDocumentSnapshot in queryDocumentSnapshots) {
                        val card: Card = document.toObject(
                            Card::class.java
                        )
                        card.path = document.getReference().getPath()
                        firestore!!.collection("bouquets").document(document.getId())
                            .update("path", document.getReference().getPath())
                        cards!!.cards.add(card)
                    }

                    if (firebaseAuth!!.getCurrentUser() != null) {
                        val UID: String = firebaseAuth!!.getCurrentUser()!!.getUid()
                        cards!!.UID = UID
                        Log.i("TAG", cards!!.UID!!)
                        firestore!!.collection("users").document(UID).get()
                            .addOnSuccessListener(object : OnSuccessListener<DocumentSnapshot> {
                                override fun onSuccess(documentSnapshot: DocumentSnapshot) {
                                    val user: User? = documentSnapshot.toObject(
                                        User::class.java
                                    )
                                    val favouriteCardsList: List<String?>? =
                                        user!!.favouriteCardsPath
                                    val toShoppingCardList: List<String?>? =
                                        user!!.cardsToShoppingCartPath
                                    cards!!.phoneNumber = user.phoneNumber
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
                                    homeFragment = HomeFragment()
                                    val homeBundle: Bundle = Bundle()
                                    homeBundle.putSerializable("cards", cards)
                                    homeFragment!!.setArguments(homeBundle)
                                    Log.i("TAG", "UID: " + cards!!.UID)
                                    Log.i("TAG", "fav: " + cards!!.favouriteCards.size.toString())
                                    Log.i(
                                        "TAG",
                                        "shop: " + cards!!.toShoppingCartCards.size.toString()
                                    )
                                    getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment, homeFragment!!).commit()
                                }
                            })
                    } else {
                        homeFragment = HomeFragment()
                        val homeBundle: Bundle = Bundle()
                        homeBundle.putSerializable("cards", cards)
                        homeFragment!!.setArguments(homeBundle)
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, homeFragment!!).commit()
                    }


                }
            })
    }*/
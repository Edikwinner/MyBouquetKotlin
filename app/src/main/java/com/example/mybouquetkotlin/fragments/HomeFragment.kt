package com.example.mybouquetkotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.data.Card
import com.example.mybouquetkotlin.data.Cards
import com.example.mybouquetkotlin.data.User
import com.example.mybouquetkotlin.recycler_view_home_screen.HomeScreenAdapter
import com.google.android.gms.tasks.OnSuccessListener

class HomeFragment() : Fragment() {
    private var homeScreenAdapter: HomeScreenAdapter? = null
    private var cards: Cards? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            cards = requireArguments()["cards"] as Cards?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.home_screen_recycler_view)

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        homeScreenAdapter = HomeScreenAdapter(cards!!.cards)
        homeScreenAdapter!!.setOnClickListener(object : HomeScreenAdapter.customOnClickListener {
            override fun onFavouriteButtonClicked(card: Card?, button: ImageButton) {
                if (cards!!.firebaseAuth!!.currentUser != null) {
                    val UID = cards!!.firebaseAuth!!.currentUser!!.uid
                   var isCardInFireBase = false
                    cards!!.firestore!!.collection("users").document(UID).get()
                        .addOnSuccessListener { documentSnapshot ->
                            val user = documentSnapshot.toObject(
                                User::class.java
                            )
                            for (string: String? in user!!.favouriteCardsPath) {
                                if ((string == card!!.path)) {
                                  isCardInFireBase = true
                                    break
                                }
                            }
                            if (!isCardInFireBase) {
                                Log.i("TAG", card!!.path)
                                button.setImageResource(R.drawable.favourite_clicked)
                                cards!!.addCardToFavouriteCards(card)
                                Log.i("TAG", "onFavouriteButtonClicked: added")
                            } else {
                                button.setImageResource(R.drawable.favorite)
                                cards!!.deleteCardFromFavouriteCards(card)
                                Log.i("TAG", "onFavouriteButtonClicked: removed")
                            }
                        }
                } else {
                    Toast.makeText(context, "Please, sign in", Toast.LENGTH_LONG).show()
                }
            }

            override fun onAddToShoppingCartClicked(card: Card?, button: Button, button2: Button) {
                if (cards!!.firebaseAuth!!.currentUser != null) {
                    val UID = cards!!.firebaseAuth!!.currentUser!!.uid
                   var isCardInFireBase = false
                    cards!!.firestore!!.collection("users").document(UID).get()
                        .addOnSuccessListener(
                            OnSuccessListener { documentSnapshot ->
                                val user = documentSnapshot.toObject(
                                    User::class.java
                                )
                                for (string: String? in user!!.cardsToShoppingCartPath) {
                                    if ((string == card!!.path)) {
                                      isCardInFireBase = true
                                        break
                                    }
                                }
                                if (!isCardInFireBase) {
                                    cards!!.addCardToShoppingCards(card)
                                    button.visibility = View.GONE
                                    button2.visibility = View.VISIBLE

                                    Log.i("TAG", "onFavouriteButtonClicked: added")
                                } else {
                                    cards!!.deleteCardFromShoppingCards(card)
                                    button.visibility = View.GONE
                                    button2.visibility = View.VISIBLE
                                    Log.i("TAG", "onFavouriteButtonClicked: removed")
                                }
                            })
                } else {
                    Toast.makeText(context, "Please, sign in", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCardViewClicked(card: Card?) {
                Log.i("TAG", "onCardViewClicked: ")
                val descriptionFragment = DescriptionFragment()
                val discriptionBundle = Bundle()
                discriptionBundle.putSerializable("card", card)
                discriptionBundle.putSerializable("cards", cards)
                discriptionBundle.putString("fragment", "home")
                descriptionFragment.arguments = discriptionBundle
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, descriptionFragment).commit()
            }
        })
        homeScreenAdapter!!.setFavouriteCards(cards!!.favouriteCards)
        homeScreenAdapter!!.setShoppingCards(cards!!.toShoppingCartCards)
        recyclerView.adapter = homeScreenAdapter

        return rootView
    }
}
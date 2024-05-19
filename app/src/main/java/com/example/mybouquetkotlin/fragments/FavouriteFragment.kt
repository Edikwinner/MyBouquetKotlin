package com.example.mybouquetkotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
import com.google.firebase.firestore.DocumentSnapshot

class FavouriteFragment() : Fragment() {
    private var cards: Cards? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            cards = requireArguments().get("cards") as Cards?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_favourite, container, false)
        if (cards!!.UID == null) {
            val toast: Toast = Toast.makeText(
                getContext(),
                "Для просмотра избранных букетов необходимо зарегистрироваться",
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        val recyclerView: RecyclerView = rootView.findViewById(R.id.favourite_screen_recycler_view)
        recyclerView.setLayoutManager(GridLayoutManager(getContext(), 2))

        val homeScreenAdapter: HomeScreenAdapter = HomeScreenAdapter(cards!!.favouriteCards)

        homeScreenAdapter.setOnClickListener(object : HomeScreenAdapter.customOnClickListener {
            override fun onFavouriteButtonClicked(card: Card?, button: ImageButton) {
                if (cards!!.firebaseAuth!!.getCurrentUser() != null) {
                    val UID: String = cards!!.firebaseAuth!!.getCurrentUser()!!.getUid()
                    var isCardInFireBase = false
                    cards!!.firestore!!.collection("users").document(UID).get()
                        .addOnSuccessListener(object : OnSuccessListener<DocumentSnapshot> {
                            override fun onSuccess(documentSnapshot: DocumentSnapshot) {
                                val user: User? = documentSnapshot.toObject(
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
                        })
                } else {
                    Toast.makeText(getContext(), "Please, sign in", Toast.LENGTH_LONG).show()
                }
            }

            override fun onAddToShoppingCartClicked(card: Card?, button: Button, button2: Button) {
                if (cards!!.firebaseAuth!!.getCurrentUser() != null) {
                    val UID: String = cards!!.firebaseAuth!!.getCurrentUser()!!.getUid()
                    var isCardInFireBase = false
                    cards!!.firestore!!.collection("users").document(UID).get()
                        .addOnSuccessListener(object : OnSuccessListener<DocumentSnapshot> {
                            override fun onSuccess(documentSnapshot: DocumentSnapshot) {
                                val user: User? = documentSnapshot.toObject(
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
                                    button.setVisibility(View.GONE)
                                    button2.setVisibility(View.VISIBLE)

                                    Log.i("TAG", "onFavouriteButtonClicked: added")
                                } else {
                                    cards!!.deleteCardFromShoppingCards(card)
                                    button.setVisibility(View.GONE)
                                    button2.setVisibility(View.VISIBLE)
                                    Log.i("TAG", "onFavouriteButtonClicked: removed")
                                }
                            }
                        })
                } else {
                    Toast.makeText(getContext(), "Please, sign in", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCardViewClicked(card: Card?) {
                val descriptionFragment: DescriptionFragment = DescriptionFragment()
                val discriptionBundle: Bundle = Bundle()
                discriptionBundle.putSerializable("card", card)
                discriptionBundle.putSerializable("cards", cards)
                discriptionBundle.putString("fragment", "fav")
                descriptionFragment.setArguments(discriptionBundle)
                requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, descriptionFragment).commit()
            }
        })
        homeScreenAdapter.setFavouriteCards(cards!!.favouriteCards)
        homeScreenAdapter.setShoppingCards(cards!!.toShoppingCartCards)
        recyclerView.setAdapter(homeScreenAdapter)
        return rootView
    }
}
package com.example.mybouquetkotlin.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.data.Card
import com.example.mybouquetkotlin.data.Cards
import com.squareup.picasso.Picasso

class DescriptionFragment() : Fragment() {
    private var card: Card? = null
    private var cards: Cards? = null
    var fragment: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            card = requireArguments().get("card") as Card?
            cards = requireArguments().get("cards") as Cards?
            fragment = requireArguments().getString("fragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var isLiked: Boolean = false
        var isShop: Boolean = false

        val rootView: View = inflater.inflate(R.layout.fragment_description, container, false)
        val bouquetName: TextView = rootView.findViewById(R.id.bouquet_name_description)
        val bouquetDescription: TextView = rootView.findViewById(R.id.bouquet_description)
        val bouquetImage: ImageView = rootView.findViewById(R.id.bouquet_image_description)

        val addToFavourite: ImageButton = rootView.findViewById(R.id.add_to_favourite_description)
        val removeFromFavourite: ImageButton =
            rootView.findViewById(R.id.remove_from_favourite_description)
        val addToShoppingCart: Button = rootView.findViewById(R.id.add_to_shopping_cart_description)
        val addToShoppingCartTrue: Button =
            rootView.findViewById(R.id.add_to_shopping_cart_description_true)

        for (card1: Card? in cards!!.favouriteCards) {
            if (card1 === card) {
                isLiked = true
                break
            }
        }

        for (card2: Card? in cards!!.toShoppingCartCards) {
            if (card2 === card) {
                isShop = true
                break
            }
        }

        if (isLiked) {
            addToFavourite.setVisibility(View.GONE)
            removeFromFavourite.setVisibility(View.VISIBLE)
        } else {
            removeFromFavourite.setVisibility(View.GONE)
            addToFavourite.setVisibility(View.VISIBLE)
        }
        if (isShop) {
            addToShoppingCart.setVisibility(View.GONE)
            addToShoppingCartTrue.setVisibility(View.VISIBLE)
        } else {
            addToShoppingCartTrue.setVisibility(View.GONE)
            addToShoppingCart.setVisibility(View.VISIBLE)
        }

        bouquetName.setText(card!!.bouquetName)
        bouquetDescription.setText(card!!.bouquetDescription)
        addToShoppingCart.setText(card!!.bouquetCost.toString() + " ₽")
        Picasso.get().load(Uri.parse(card!!.bouquetImage)).into(bouquetImage)

        rootView.findViewById<View>(R.id.return_to_fragment)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (fragment === "home") {
                        val homeFragment: HomeFragment = HomeFragment()
                        val homeBundle: Bundle = Bundle()
                        homeBundle.putSerializable("cards", cards)
                        homeFragment.setArguments(homeBundle)
                        requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, homeFragment).commit()
                    } else if (fragment === "fav") {
                        val favouriteFragment: FavouriteFragment = FavouriteFragment()
                        val favouriteBundle: Bundle = Bundle()
                        favouriteBundle.putSerializable("cards", cards)
                        favouriteFragment.setArguments(favouriteBundle)
                        requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, favouriteFragment).commit()
                    }
                }
            })
        addToShoppingCart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cards!!.addCardToShoppingCards(card)
                addToShoppingCart.setVisibility(View.GONE)
                addToShoppingCartTrue.setVisibility(View.VISIBLE)
            }
        })

        addToShoppingCartTrue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cards!!.deleteCardFromShoppingCards(card)
                addToShoppingCartTrue.setVisibility(View.GONE)
                addToShoppingCart.setVisibility(View.VISIBLE)
            }
        })

        addToFavourite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cards!!.addCardToFavouriteCards(card)
                addToFavourite.setVisibility(View.GONE)
                removeFromFavourite.setVisibility(View.VISIBLE)
            }
        })
        removeFromFavourite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                cards!!.deleteCardFromFavouriteCards(card)
                removeFromFavourite.setVisibility(View.GONE)
                addToFavourite.setVisibility(View.VISIBLE)
            }
        })



        return rootView
    }
}
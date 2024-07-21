package com.example.mybouquetkotlin.View.Fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.ViewModel.Fragments.DescriptionViewModel
import com.example.mybouquetkotlin.databinding.FragmentDescriptionBinding
import com.squareup.picasso.Picasso

class DescriptionFragment : Fragment() {
    private lateinit var viewModel:DescriptionViewModel
    private lateinit var binding: FragmentDescriptionBinding
    private lateinit var card: Card
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = requireArguments().get("card") as Card
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[DescriptionViewModel::class.java]
        binding.toolbar.setNavigationOnClickListener {
            binding.root.findNavController().popBackStack()
        }
        binding.bouquetDescription.text = card.bouquetDescription
        binding.bouquetNameDescription.text = card.bouquetName
        Picasso.get().load(Uri.parse(card.bouquetImage)).into(binding.bouquetImageDescription)
        binding.addToShoppingCartDescription.text = card.bouquetCost.toString() + " ₽"

        binding.addToShoppingCartDescription.setOnClickListener {
            viewModel.addToShoppingCards(card)
            viewModel.shoppingCards.observe(viewLifecycleOwner, Observer {
                if(!it.contains(card)){
                    binding.addToShoppingCartDescription.text = card.bouquetCost.toString() + " ₽"
                }
                else{
                    binding.addToShoppingCartDescription.text = "Удалить"
                }
            })

        }

        binding.addToFavouriteDescription.setOnClickListener{
            viewModel.addToFavouriteCards(card)
            viewModel.favouriteCards.observe(viewLifecycleOwner, Observer {
                if(!it.contains(card)){
                    binding.addToFavouriteDescription.setImageResource(R.drawable.favorite)
                }
                else{
                    binding.addToFavouriteDescription.setImageResource(R.drawable.favourite_clicked)
                }
            })
        }

        viewModel.favouriteCards.observe(viewLifecycleOwner, Observer {
            if(card in it){
                binding.addToFavouriteDescription.setImageResource(R.drawable.favourite_clicked)
            }
            else{
                binding.addToFavouriteDescription.setImageResource(R.drawable.favorite)
            }
        })

        viewModel.shoppingCards.observe(viewLifecycleOwner, Observer {
            if(card in it){
                binding.addToShoppingCartDescription.text = "Удалить"
            }
            else{
                binding.addToShoppingCartDescription.text = card.bouquetCost.toString() + " ₽"
            }
        })
        return binding.root
    }
}

       /* val bouquetName: TextView = rootView.findViewById(R.id.bouquet_name_description)
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


*/

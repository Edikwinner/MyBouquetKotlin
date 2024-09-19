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
import com.example.mybouquetkotlin.ViewModel.Fragments.AddViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.DescriptionViewModel
import com.example.mybouquetkotlin.databinding.FragmentDescriptionBinding
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class DescriptionFragment : Fragment() {
    private val viewModel by viewModel<DescriptionViewModel>()
    private lateinit var binding: FragmentDescriptionBinding
    private lateinit var card: Card
    private var isFavourite = false
    private var isShopping = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = requireArguments().get("card") as Card
        isShopping = requireArguments().getBoolean("shopping")
        isFavourite = requireArguments().getBoolean("favourite")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(inflater)
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

        if(isFavourite){
            binding.addToFavouriteDescription.setImageResource(R.drawable.favourite_clicked)
        }
        else{
            binding.addToFavouriteDescription.setImageResource(R.drawable.favorite)
        }


        if(isShopping){
            binding.addToShoppingCartDescription.text = "Удалить"
        }
        else{
            binding.addToShoppingCartDescription.text = card.bouquetCost.toString() + " ₽"
        }

        return binding.root
    }
}
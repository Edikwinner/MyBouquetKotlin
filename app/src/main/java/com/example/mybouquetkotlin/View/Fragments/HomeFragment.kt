package com.example.mybouquetkotlin.View.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mybouquetkotlin.Model.Adapters.HomeScreenAdapter
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.ViewModel.Fragments.HomeViewModel
import com.example.mybouquetkotlin.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), HomeScreenAdapter.customListener{
    private val viewModel by viewModel<HomeViewModel>()
    private lateinit var adapter: HomeScreenAdapter
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        viewModel.refreshData()
        adapter = HomeScreenAdapter(viewModel.cards.value ?: ArrayList(), this, viewModel)
        binding.homeScreenRecyclerView.adapter = adapter
        viewModel.cards.observe(viewLifecycleOwner, Observer {
            adapter = HomeScreenAdapter(it, this, viewModel)
            binding.homeScreenRecyclerView.adapter = adapter
        })
        binding.homeScreenRecyclerView.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }

    override fun onFavouriteButtonClicked(card: Card, button: ImageButton) {
        if (viewModel.currentUser.value != null) {
            viewModel.addToFavouriteCards(card)
            viewModel.favouriteCards.observe(viewLifecycleOwner, Observer {
                if (!it.contains(card)) {
                    button.setImageResource(R.drawable.favorite)
                } else {
                    button.setImageResource(R.drawable.favourite_clicked)
                }
            })
        }
        else{
            Log.i("TAG", "no")
        }
    }

    override fun onAddToShoppingCartClicked(card: Card, button: Button) {
        if(viewModel.currentUser.value != null){
            viewModel.addToShoppingCards(card)
            viewModel.shoppingCards.observe(viewLifecycleOwner, Observer {
                if(!it.contains(card)){
                    button.text = card.bouquetCost.toString() + " ₽"
                }
                else{
                    button.text = "Удалить"
                }
            })
        }
        else{
            Log.i("TAG", "no ")
        }
    }

    override fun onCardViewClicked(card: Card) {
        val bundle = Bundle()
        bundle.putSerializable("card", card)
        bundle.putBoolean("favourite", viewModel.favouriteCards.value!!.contains(card))
        bundle.putBoolean("shopping", viewModel.shoppingCards.value!!.contains(card))
        binding.root.findNavController().navigate(R.id.action_homeFragment_to_descriptionFragment, bundle)
        Log.i("TAG", "onCardViewClicked: ")
    }

}
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
import com.example.mybouquetkotlin.ViewModel.Fragments.AddViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.HomeViewModel
import com.example.mybouquetkotlin.databinding.FragmentFavouriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment(), HomeScreenAdapter.customListener{
    private val viewModel by viewModel<HomeViewModel>()
    private lateinit var adapter: HomeScreenAdapter
    private lateinit var binding: FragmentFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater)
        viewModel.refreshData()

        binding.favouriteScreenRecyclerView.setLayoutManager(GridLayoutManager(context, 2))
        adapter = HomeScreenAdapter(viewModel.favouriteCards.value ?: ArrayList(), this, viewModel)
        binding.favouriteScreenRecyclerView.adapter = adapter
        viewModel.favouriteCards.observe(viewLifecycleOwner, Observer {
            adapter = HomeScreenAdapter(it, this, viewModel)
            binding.favouriteScreenRecyclerView.adapter = adapter
        })
        return binding.root
    }

    override fun onFavouriteButtonClicked(card: Card, button: ImageButton) {
        Log.i("TAG", "onFavouriteButtonClicked: ")
        viewModel.addToFavouriteCards(card)
    }

    override fun onAddToShoppingCartClicked(card: Card, button: Button) {
        Log.i("TAG", "onAddToShoppingCartClicked: ")
        viewModel.addToShoppingCards(card)
    }

    override fun onCardViewClicked(card: Card) {
        val bundle = Bundle()
        bundle.putSerializable("card", card)
        bundle.putBoolean("favourite", viewModel.favouriteCards.value!!.contains(card))
        bundle.putBoolean("shopping", viewModel.shoppingCards.value!!.contains(card))
        binding.root.findNavController().navigate(R.id.action_favouriteFragment_to_descriptionFragment, bundle)
        Log.i("TAG", "onCardViewClicked: ")
    }
}
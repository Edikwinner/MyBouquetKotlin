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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybouquetkotlin.R
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Adapters.HomeScreenAdapter
import com.example.mybouquetkotlin.ViewModel.Activities.MainActivityViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.HomeViewModel

class HomeFragment() : Fragment(), HomeScreenAdapter.customListener{
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.home_screen_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        return rootView
    }

    override fun onFavouriteButtonClicked(card: Card, button: ImageButton) {
        Log.i("TAG", "onFavouriteButtonClicked: ")
    }

    override fun onAddToShoppingCartClicked(card: Card, button: Button, button2: Button) {

    }

    override fun onCardViewClicked(card: Card) {

    }

}
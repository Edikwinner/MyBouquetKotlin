package com.example.mybouquetkotlin.ViewModel.Fragments

import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Adapters.ShoppingCartScreenAdapter
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import com.example.mybouquetkotlin.View.Fragments.ShoppingCardFragment
import com.example.mybouquetkotlin.ViewModel.Activities.MainActivityViewModel
import kotlinx.coroutines.launch

class ShoppingCardViewModel: ViewModel(), ShoppingCartScreenAdapter.customOnClickListener {
    val mainActivityViewModel = MainActivityViewModel()
    val cardRepository = CardRepository()
    var shoppingCards = MutableLiveData<List<Card>>()
    val totalSum = MutableLiveData<Int>()

    val scope = viewModelScope.launch {
        shoppingCards.value =
            cardRepository.getShoppingCards(mainActivityViewModel.currentUser.value)
        if (shoppingCards.value != null){
            for (card in shoppingCards.value!!) {
                totalSum.value = card.bouquetCost + totalSum.value!!
            }
        }
    }

    fun makeOrder(paths: List<String>){
        val scope = viewModelScope.launch {
            for (path in paths) {
                cardRepository.makeOrder(path)
            }
        }
    }

    override fun deleteCard(card: Card?) {

    }
}
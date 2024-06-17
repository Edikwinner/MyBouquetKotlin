package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Adapters.HomeScreenAdapter
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import com.example.mybouquetkotlin.ViewModel.Activities.MainActivityViewModel
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel(){

    var cardRepository = CardRepository()
    var cards = MutableLiveData<ArrayList<Card>>()
    val mainActivityViewModel = MainActivityViewModel()

    init {
        viewModelScope.launch {
            cards.value = cardRepository.getAllCards()
        }
    }

    fun addToFavouriteCards(card: Card){
        viewModelScope.launch {
            cardRepository.addCardToFavouriteCards(card)
        }
    }



}
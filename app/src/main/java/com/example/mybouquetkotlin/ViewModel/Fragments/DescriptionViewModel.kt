package com.example.mybouquetkotlin.ViewModel.Fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class DescriptionViewModel(val cardRepository: CardRepository): ViewModel() {
    val favouriteCards = MutableLiveData<ArrayList<Card>>()
    val shoppingCards = MutableLiveData<ArrayList<Card>>()

    init {
        viewModelScope.launch {
            favouriteCards.value = cardRepository.getFavouriteCards(cardRepository.getUser())
            shoppingCards.value = cardRepository.getShoppingCards(cardRepository.getUser())
        }
    }

    fun addToShoppingCards(card: Card){
        viewModelScope.launch {
            if(shoppingCards.value?.contains(card) == false){
                Log.i("TAG", "addToShoppingCards: ")
                cardRepository.addCardToShoppingCards(card)
                shoppingCards.value!!.add(card)
            }
            else{
                Log.i("TAG", "deleteFromShoppingCards: ")
                cardRepository.deleteCardFromShoppingCards(card)
                shoppingCards.value!!.remove(card)
            }
        }
    }

    fun addToFavouriteCards(card: Card){
        viewModelScope.launch {
            if(favouriteCards.value?.contains(card) == false){
                Log.i("TAG", "addToFavouriteCards: ")
                cardRepository.addCardToFavouriteCards(card)
                favouriteCards.value!!.add(card)
            }
            else{
                Log.i("TAG", "removeFromFavouriteCards: ")
                cardRepository.deleteCardFromFavouriteCards(card)
                favouriteCards.value!!.remove(card)
            }
        }
    }
}
package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class ShoppingCardViewModel: ViewModel(){

    val totalSum = MutableLiveData<Int>()
    val cardRepository = CardRepository()
    val currentUser = MutableLiveData<User?>()
    val shoppingCards = MutableLiveData<ArrayList<Card>>(ArrayList())

    init {
        viewModelScope.launch {
            currentUser.value = cardRepository.getUser()
            shoppingCards.value = cardRepository.getShoppingCards(currentUser.value)
        }
    }

    fun makeOrder(paths: ArrayList<String>){
        viewModelScope.launch {
            for (path in paths) {
                cardRepository.makeOrder(path)
            }
        }
    }
    fun deleteCard(card: Card) {
        shoppingCards.value!!.remove(card)
        cardRepository.deleteCardFromShoppingCards(card)
    }

    fun deleteCards(){
        for (card in this.shoppingCards.value!!){
            cardRepository.deleteCardFromShoppingCards(card)
        }
        this.shoppingCards.value = ArrayList()
    }

    fun refreshData(){
        viewModelScope.launch {
            currentUser.value = cardRepository.getUser()
            shoppingCards.value = cardRepository.getShoppingCards(currentUser.value)
        }
    }
}
package com.example.mybouquetkotlin.ViewModel.Fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(val cardRepository: CardRepository):ViewModel(){

    val currentUser = MutableLiveData<User?>()
    val cards =  MutableLiveData<List<Card>>()
    val favouriteCards = MutableLiveData<ArrayList<Card>>()
    val shoppingCards = MutableLiveData<ArrayList<Card>>()

    init {
        viewModelScope.launch {

            currentUser.value = cardRepository.getUser()
            favouriteCards.value = cardRepository.getFavouriteCards(currentUser.value)
            shoppingCards.value = cardRepository.getShoppingCards(currentUser.value)
            cards.value = cardRepository.getAllCards()


            Log.i("TAG1", "f: " + favouriteCards.value!!.size.toString())
            Log.i("TAG1", "s: " + shoppingCards.value!!.size.toString())
            Log.i("TAG1", "_____________")
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
            Log.i("TAG1", "f: " + favouriteCards.value!!.size.toString())
            Log.i("TAG1", "s: " + shoppingCards.value!!.size.toString())
            Log.i("TAG1", "_____________")
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
            Log.i("TAG1", "f: " + favouriteCards.value!!.size.toString())
            Log.i("TAG1", "s: " + shoppingCards.value!!.size.toString())
            Log.i("TAG1", "_____________")
        }
    }

    fun refreshData(){
        viewModelScope.launch {
            currentUser.value = cardRepository.getUser()
            favouriteCards.value = cardRepository.getFavouriteCards(currentUser.value)
            shoppingCards.value = cardRepository.getShoppingCards(currentUser.value)
            cards.value = cardRepository.getAllCards()
        }
    }

}
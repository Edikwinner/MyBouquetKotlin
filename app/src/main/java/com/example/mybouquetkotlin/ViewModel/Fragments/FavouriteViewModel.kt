package com.example.mybouquetkotlin.ViewModel.Fragments

import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Adapters.HomeScreenAdapter
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class FavouriteViewModel: ViewModel(), HomeScreenAdapter.customListener{
    var user = MutableLiveData<User?>()
    val cardRepository = CardRepository()
    var favouriteCards = MutableLiveData<List<Card>?>()

    val scope = viewModelScope.launch {
        user.value = cardRepository.getUser()
        favouriteCards.value = cardRepository.getFavouriteCards(user.value)

    }

    override fun onFavouriteButtonClicked(card: Card, button: ImageButton) {

    }

    override fun onAddToShoppingCartClicked(card: Card, button: Button, button2: Button) {

    }

    override fun onCardViewClicked(card: Card) {

    }
}
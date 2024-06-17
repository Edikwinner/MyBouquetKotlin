package com.example.mybouquetkotlin.ViewModel.Activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {
    val cardRepository = CardRepository()

    val currentUser = MutableLiveData<User?>()
    val cards = MutableLiveData<List<Card>>()

    init {
        viewModelScope.launch {
            currentUser.value = cardRepository.getUser()
            cards.value = cardRepository.getAllCards()
        }
    }

}
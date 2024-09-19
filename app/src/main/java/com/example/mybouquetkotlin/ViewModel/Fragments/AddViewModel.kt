package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class AddViewModel(val cardRepository: CardRepository):ViewModel() {

    fun makeOrder(description: String){
        viewModelScope.launch {
            cardRepository.makeOrder(description)
        }
    }
}
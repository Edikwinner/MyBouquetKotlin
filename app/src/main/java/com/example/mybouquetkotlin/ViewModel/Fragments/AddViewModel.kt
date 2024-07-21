package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class AddViewModel:ViewModel() {
    val cardRepository = CardRepository()

    fun makeOrder(description: String){
        viewModelScope.launch {
            cardRepository.makeOrder(description)
        }
    }
}
package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import com.example.mybouquetkotlin.ViewModel.Activities.MainActivityViewModel
import kotlinx.coroutines.launch

class AddViewModel:ViewModel() {
    val cardRepository = CardRepository()
    val mainActivityViewModel = MainActivityViewModel()

    fun makeOrder(description: String){
        val scope = viewModelScope.launch {
            cardRepository.makeOrder(description)
        }
    }
}
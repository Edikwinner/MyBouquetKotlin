package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import com.example.mybouquetkotlin.ViewModel.Activities.MainActivityViewModel
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    val cardRepository = CardRepository()
    val mainActivityViewModel = MainActivityViewModel()
    fun logOff(){
        val scope = viewModelScope.launch {
            cardRepository.signOff()
            mainActivityViewModel.currentUser.value = null
        }
    }

    fun savePhoneNumber(phoneNumber: String){
        val scope = viewModelScope.launch {
            cardRepository.saveUserNumber(phoneNumber)
            mainActivityViewModel.currentUser.value!!.phoneNumber = phoneNumber
        }
    }

}
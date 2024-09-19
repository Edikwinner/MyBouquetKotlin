package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class UserViewModel(val cardRepository: CardRepository): ViewModel() {
    val currentUser = MutableLiveData<User?>()
    init {
        viewModelScope.launch {
            currentUser.value = cardRepository.getUser()
        }
    }
    fun logOff(){
        viewModelScope.launch {
            cardRepository.signOff()
            currentUser.value = null
        }
    }

    fun savePhoneNumber(phoneNumber: String){
        viewModelScope.launch {
            cardRepository.saveUserNumber(phoneNumber)
            currentUser.value!!.phoneNumber = phoneNumber
        }
    }

    fun refreshData(){
        viewModelScope.launch {
            currentUser.value = cardRepository.getUser()
        }
    }

}
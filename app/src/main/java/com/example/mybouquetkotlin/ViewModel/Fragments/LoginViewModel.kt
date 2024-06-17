package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import com.example.mybouquetkotlin.ViewModel.Activities.MainActivityViewModel
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    val cardRepository = CardRepository()
    val mainActivityViewModel = MainActivityViewModel()

    fun signIn(email:String, password:String){
        val scope = viewModelScope.launch {
            if(cardRepository.signIn(email, password)){
                mainActivityViewModel.currentUser.value = cardRepository.getUser()
            }
        }
    }

    fun createUser(email:String, password:String){
        val scope = viewModelScope.launch {
            if(cardRepository.createUser(email, password)){
                signIn(email, password)
            }
        }
    }
}
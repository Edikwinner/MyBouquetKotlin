package com.example.mybouquetkotlin.ViewModel.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    val cardRepository = CardRepository()
    val currentUser = MutableLiveData<User?>()
    init {
        viewModelScope.launch {
            currentUser.value = cardRepository.getUser()
        }
    }

    fun signIn(email:String, password:String){
        viewModelScope.launch {
            if(cardRepository.signIn(email, password)){
               currentUser.value = cardRepository.getUser()
            }
        }
    }

    fun createUser(email:String, password:String){
        viewModelScope.launch {
            if(cardRepository.createUser(email, password)){
                signIn(email, password)
            }
        }
    }
}
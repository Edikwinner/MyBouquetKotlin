package com.example.mybouquetkotlin.ViewModel.Activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybouquetkotlin.Model.Entity.User
import com.example.mybouquetkotlin.Model.Repository.CardRepository
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {
    val cardRepository = CardRepository()
    val user = MutableLiveData<User?>()

    init {
        viewModelScope.launch {
            user.value = cardRepository.getUser()
        }
    }
}
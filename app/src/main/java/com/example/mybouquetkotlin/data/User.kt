package com.example.mybouquetkotlin.data

data class User(var iD: String? = "",
                var phoneNumber: String = "",
                var favouriteCardsPath: List<String> = ArrayList(),
                var cardsToShoppingCartPath: List<String> = ArrayList(),
                var orders: List<String> = ArrayList());

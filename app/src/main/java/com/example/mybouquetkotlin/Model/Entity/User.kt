package com.example.mybouquetkotlin.Model.Entity

data class User(var iD: String = "",
                var phoneNumber: String = "",
                var favouriteCardsPath: List<String> = ArrayList(),
                var shoppingCardsPath: List<String> = ArrayList(),
                var orders: List<String> = ArrayList())

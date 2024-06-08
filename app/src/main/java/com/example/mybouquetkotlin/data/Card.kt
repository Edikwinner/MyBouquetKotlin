package com.example.mybouquetkotlin.data

import java.io.Serializable

data class Card(var path: String = "",
                var bouquetName: String = "",
                var bouquetCost: Int = 0,
                var bouquetImage: String = "",
                var bouquetDescription: String = "") : Serializable;

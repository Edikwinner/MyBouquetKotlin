package com.example.mybouquetkotlin.data

import java.io.Serializable

class Card : Serializable {
    var path: String = ""
    var bouquetName: String = ""
    var bouquetCost: Int = 0
    var bouquetImage: String = ""
    var bouquetDescription: String = ""

    constructor()

    constructor(
        bouquetName: String,
        bouquetCost: Int,
        bouquetImage: String,
        bouquetDescription: String
    ) {
        this.bouquetName = bouquetName
        this.bouquetCost = bouquetCost
        this.bouquetImage = bouquetImage
        this.bouquetDescription = bouquetDescription
    }
}

package com.example.mybouquetkotlin.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable


class Cards : Serializable {
    var UID: String? = null
    var cards: MutableList<Card?> = ArrayList()
    var favouriteCards: MutableList<Card?> = ArrayList()
    var toShoppingCartCards: MutableList<Card?> = ArrayList()
    var ordersList: List<String> = ArrayList()
    var phoneNumber: String? = ""
    var firestore: FirebaseFirestore? = null
    var firebaseAuth: FirebaseAuth? = null
    fun addCardToFavouriteCards(card: Card?) {
        firestore!!.collection("users")
            .document(UID!!)
            .update("favouriteCardsPath", FieldValue.arrayUnion(card!!.path))
        favouriteCards.add(card)
    }

    fun deleteCardFromFavouriteCards(card: Card?) {
        firestore!!.collection("users")
            .document(UID!!)
            .update("favouriteCardsPath", FieldValue.arrayRemove(card!!.path))
        favouriteCards.remove(card)
    }

    fun addCardToShoppingCards(card: Card?) {
        firestore!!.collection("users")
            .document(UID!!)
            .update("cardsToShoppingCartPath", FieldValue.arrayUnion(card!!.path))
        toShoppingCartCards.add(card)
    }

    fun deleteCardFromShoppingCards(card: Card?) {
        firestore!!.collection("users")
            .document(UID!!)
            .update("cardsToShoppingCartPath", FieldValue.arrayRemove(card!!.path))
        toShoppingCartCards.remove(card)
    }

    fun deleteAllFromShoppingCards() {
        for (card in this.toShoppingCartCards) {
            firestore!!.collection("users")
                .document(UID!!)
                .update("cardsToShoppingCartPath", FieldValue.arrayRemove(card!!.path))
        }
        toShoppingCartCards.clear()
    }

    fun deleteAllFromFavouriteCards() {
        for (card in this.favouriteCards) {
            deleteCardFromFavouriteCards(card)
        }
    }

    fun addPhoneNumberToUser(phoneNumber: String?) {
        this.phoneNumber = phoneNumber
        firestore!!.collection("users")
            .document(UID!!)
            .update("phoneNumber", phoneNumber)
    }

    fun addOrder(path: String?) {
        firestore!!.collection("users")
            .document(UID!!)
            .update("orders", FieldValue.arrayUnion(path))
    }
}

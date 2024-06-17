package com.example.mybouquetkotlin.Model.Repository

import android.util.Log
import com.example.mybouquetkotlin.Model.Cards
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CardRepository() {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun getAllCards(): ArrayList<Card> = suspendCoroutine { continuation ->
        val cards = ArrayList<Card>()
        firestore
            .collection("bouquets")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (document in queryDocumentSnapshots) {
                    val card = document.toObject<Card>()
                    cards.add(card)
                }
                continuation.resume(cards)
            }
    }

    suspend fun getUser(): User? {
        if (firebaseAuth.currentUser != null) {
            val UID = firebaseAuth.currentUser!!.uid.toString()
            return try {
                firestore
                    .collection("users")
                    .document(UID)
                    .get()
                    .await()
                    .toObject(User::class.java)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    suspend fun getFavouriteCards(user: User?): List<Card>? = suspendCoroutine { continuation ->
        if(user != null) {
            val favouritePaths = user.favouriteCardsPath
            val favouriteCards = ArrayList<Card>()
            firestore
                .collection("bouquets")
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    for (document in queryDocumentSnapshots) {
                        val card = document.toObject<Card>()
                        if (card.path in favouritePaths) {
                            favouriteCards.add(card)
                        }
                    }
                    continuation.resume(favouriteCards)
                }
        }
        else{
            continuation.resume(null)
        }
    }
    suspend fun getShoppingCards(user: User?): List<Card>? = suspendCoroutine { continuation ->
        if(user != null) {
            val shoppingPaths = user.shoppingCardsPath
            val shoppingCards = ArrayList<Card>()
            firestore
                .collection("bouquets")
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    for (document in queryDocumentSnapshots) {
                        val card = document.toObject<Card>()
                        if (card.path in shoppingPaths) {
                            shoppingCards.add(card)
                        }
                    }
                    continuation.resume(shoppingCards)
                }
        }
        else{
            continuation.resume(null)
        }
    }

    suspend fun signIn(email:String, password:String) = suspendCoroutine{ continuation ->
        var exitCode = false
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                exitCode = true
                if(exitCode){
                    continuation.resume(true)
                }
                else {
                    continuation.resume(false)
                }
            }

    }

    suspend fun createUser(email:String, password:String)= suspendCoroutine{ continuation ->
        var exitCode = false
        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                exitCode = true
                if(exitCode){
                    continuation.resume(true)
                }
                else {
                    continuation.resume(false)
                }
            }
    }
    suspend fun signOff() = suspendCoroutine {continuation ->
        firebaseAuth.signOut()
        continuation.resume(true)
    }

    suspend fun saveUserNumber(phoneNumber: String) = suspendCoroutine { continuation ->
        val UID = firebaseAuth.uid.toString()
        firestore
            .collection("users")
            .document(UID)
            .update("phoneNumber", phoneNumber)
        continuation.resume(true)
    }
    suspend fun makeOrder(description: String) = suspendCoroutine { continuation ->
        val UID = firebaseAuth.uid.toString()
        firestore
            .collection("users")
            .document(UID)
            .update("orders", FieldValue.arrayUnion(description))
        continuation.resume(true)
    }

    fun getCurrentEmail():String?{
        return try {
            firebaseAuth.currentUser!!.email.toString()
        }
        catch (e: Exception){
            null
        }
    }

    fun addCardToFavouriteCards(card: Card) {
        val UID = firebaseAuth.uid.toString()
        firestore.collection("users")
            .document(UID)
            .update("favouriteCardsPath", FieldValue.arrayUnion(card.path))
    }

    fun deleteCardFromFavouriteCards(card: Card) {
        val UID = firebaseAuth.uid.toString()
        firestore.collection("users")
            .document(UID)
            .update("favouriteCardsPath", FieldValue.arrayRemove(card.path))
    }

    fun addCardToShoppingCards(card: Card) {
        val UID = firebaseAuth.uid.toString()
        firestore.collection("users")
            .document(UID)
            .update("cardsToShoppingCartPath", FieldValue.arrayUnion(card.path))
    }

    fun deleteCardFromShoppingCards(card: Card) {
        val UID = firebaseAuth.uid.toString()
        firestore.collection("users")
            .document(UID)
            .update("cardsToShoppingCartPath", FieldValue.arrayRemove(card.path))
    }
}
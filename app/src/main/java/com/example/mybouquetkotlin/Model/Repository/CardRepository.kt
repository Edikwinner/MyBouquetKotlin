package com.example.mybouquetkotlin.Model.Repository

import com.example.mybouquetkotlin.Model.Cards
import com.example.mybouquetkotlin.Model.Entity.Card
import com.example.mybouquetkotlin.Model.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.flow.flow

class CardRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun getAllCards():ArrayList<Card> = suspendCoroutine { continuation ->
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

    suspend fun getUser(): User? = suspendCoroutine { continuation ->
        if (firebaseAuth.currentUser != null) {
            val UID = firebaseAuth.currentUser!!.uid
            firestore
                .collection("users")
                .document(UID)
                .get()
                .addOnSuccessListener { document ->
                    val user = document.toObject<User>()
                    continuation.resume(user)
                }
        }
        else{
            continuation.resume(null)
        }
    }

    suspend fun getFavouriteCards(user: User?): ArrayList<Card> = suspendCoroutine { continuation ->
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
            continuation.resume(ArrayList())
        }
    }
    suspend fun getShoppingCards(user: User?): ArrayList<Card> = suspendCoroutine { continuation ->
        if(user != null) {
            val shoppingPaths = user.cardsToShoppingCartPath
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
            continuation.resume(ArrayList())
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
    fun makeOrder(description: String) {
        val UID = firebaseAuth.uid.toString()
        firestore
            .collection("users")
            .document(UID)
            .update("orders", FieldValue.arrayUnion(description))
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

    suspend fun getFlowCards() = flow<List<Card>> {
        val cards = firestore.collection("bouquets").get().await().toObjects(Card::class.java)
        emit(cards)
    }
    suspend fun getFlowFavouriteCards(user:User?) = flow<List<Card>?> {
        if(user != null) {
            val favouritePaths = user.favouriteCardsPath
            val favouriteCards = firestore
                .collection("bouquets")
                .whereIn("path", favouritePaths)
                .get()
                .await()
                .toObjects(Card::class.java)
            emit(favouriteCards)
        }
        else{
           emit(null)
        }
    }


    suspend fun getFlowShoppingCards() = flow<List<Card>> {
        val cards = firestore.collection("bouquets").get().await().toObjects(Card::class.java)
        emit(cards)
    }



}